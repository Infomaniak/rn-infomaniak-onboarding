/*
 Infomaniak RNOnboarding - iOS
 Copyright (C) 2025 Infomaniak Network SA

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import AuthenticationServices
import DeviceAssociation
import ExpoModulesCore
import InfomaniakCore
import InfomaniakDI
import InfomaniakLogin
import OSLog
import Sentry

public extension Capability {
    static let twoFactorAuthenticationChallengeApproval = Capability(rawValue: "2fa:push_challenge:approval")
}

@MainActor
final class LoginHandler: InfomaniakLoginDelegate, ObservableObject {
    private static let logger = Logger(subsystem: Bundle.main.bundleIdentifier ?? "RNInfomaniakOnboarding", category: "Login")

    @LazyInjectService private var loginService: InfomaniakLoginable
    @LazyInjectService private var tokenService: InfomaniakNetworkLoginable
    @LazyInjectService var deviceManager: DeviceManagerable
    @LazyInjectService var tokenStore: TokenStore

    @Published var isLoading = false

    let onLoginSuccess: EventDispatcher
    let onLoginError: EventDispatcher

    private let refreshTokenDelegate = RNInfomaniakLoginTokenDelegate()
    private let userProfileStore = UserProfileStore()

    init(onLoginSuccess: EventDispatcher, onLoginError: EventDispatcher) {
        self.onLoginSuccess = onLoginSuccess
        self.onLoginError = onLoginError
    }

    nonisolated func didCompleteLoginWith(code: String, verifier: String) {
        Task {
            await loginSuccessful(code: code, codeVerifier: verifier)
        }
    }

    nonisolated func didFailLoginWith(error: Error) {
        Task {
            await loginFailed(error: error)
        }
    }

    func loginAfterAccountCreation(from viewController: UIViewController) {
        isLoading = true
        loginService.setupWebviewNavbar(
            title: "kChat",
            titleColor: nil,
            color: nil,
            buttonColor: nil,
            clearCookie: false,
            timeOutMessage: nil
        )
        loginService.webviewLoginFrom(viewController: viewController,
                                      hideCreateAccountButton: true,
                                      delegate: self)
    }

    func login() {
        isLoading = true
        loginService.asWebAuthenticationLoginFrom(
            anchor: ASPresentationAnchor(),
            useEphemeralSession: true,
            hideCreateAccountButton: true
        ) { [weak self] result in
            switch result {
            case let .success(result):
                self?.loginSuccessful(code: result.code, codeVerifier: result.verifier)
            case let .failure(error):
                self?.loginFailed(error: error)
            }
        }
    }

    private func loginSuccessful(code: String, codeVerifier verifier: String) {
        Task {
            do {
                let token = try await tokenService.apiTokenUsing(code: code, codeVerifier: verifier)
                let temporaryApiFetcher = ApiFetcher(token: token, delegate: refreshTokenDelegate)
                let device = try await deviceManager.getOrCreateCurrentDevice()

                tokenStore.addToken(newToken: token, associatedDeviceId: device.uid)

                attachDeviceToApiToken(token, device: device, apiFetcher: temporaryApiFetcher)

                onLoginSuccess.callAsFunction(["accessToken": token.accessToken])
            } catch {
                os_log(.error, "[LoginHandler] Error: \(error)")
                onLoginError.callAsFunction(["error": error.localizedDescription])
            }

            isLoading = false
        }
    }

    private func attachDeviceToApiToken(_ token: ApiToken, device: UserDevice, apiFetcher: ApiFetcher) {
        Task {
            do {
                try await deviceManager.attachDeviceIfNeeded(device, to: token, apiFetcher: apiFetcher)
            } catch {
                os_log(.error, "[LoginHandler] Error: \(error)")
                onLoginError.callAsFunction(["error": error.localizedDescription])
            }
        }
    }

    private func loginFailed(error: Error) {
        isLoading = false
        guard (error as? ASWebAuthenticationSessionError)?.code != .canceledLogin else { return }

        onLoginError.callAsFunction(["error": error.localizedDescription])
    }
}

public final class RNInfomaniakLoginTokenDelegate: RefreshTokenDelegate, Sendable {
    public func didUpdateToken(newToken _: ApiToken, oldToken _: ApiToken) {}

    public func didFailRefreshToken(_: ApiToken) {}
}

public extension ApiFetcher {
    convenience init(token: ApiToken, delegate: RefreshTokenDelegate) {
        let decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .iso8601
        decoder.keyDecodingStrategy = .convertFromSnakeCase

        self.init(decoder: decoder)
        createAuthenticatedSession(
            token,
            authenticator: OAuthAuthenticator(refreshTokenDelegate: delegate),
            additionalAdapters: [UserAgentAdapter()]
        )
    }
}

public struct SentryService {
    public init() {
        initSentry()
    }

    private func initSentry() {
        SentrySDK.start { _ in
        }
    }
}
