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
import ExpoModulesCore
import InfomaniakDI
import InfomaniakLogin
import InfomaniakOnboarding
import OSLog
import SwiftUI

@MainActor
final class LoginHandler: InfomaniakLoginDelegate, ObservableObject {
    @LazyInjectService private var loginService: InfomaniakLoginable
    @LazyInjectService private var tokenService: InfomaniakNetworkLoginable

    @Published var isLoading = false

    let onLoginSuccess: EventDispatcher
    let onLoginError: EventDispatcher

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

                onLoginSuccess.callAsFunction(["accessToken": token.accessToken])
            } catch {
                os_log("Error fetching token: %@", type: .error, error.localizedDescription)
                onLoginError.callAsFunction(["error": error.localizedDescription])
            }

            isLoading = false
        }
    }

    private func loginFailed(error: Error) {
        isLoading = false
        guard (error as? ASWebAuthenticationSessionError)?.code != .canceledLogin else { return }

        onLoginError.callAsFunction(["error": error.localizedDescription])
    }
}

struct OnboardingBottomButtonsView: View {
    @ObservedObject var loginHandler: LoginHandler

    var body: some View {
        Button("Login") {
            loginHandler.login()
        }
    }
}

class RNInfomaniakOnboardingView: ExpoView {
    private var onboardingViewController = OnboardingViewController(configuration: .init(headerImage: nil,
                                                                                         slides: [],
                                                                                         pageIndicatorColor: nil,
                                                                                         isScrollEnabled: true,
                                                                                         dismissHandler: nil,
                                                                                         isPageIndicatorHidden: false))

    private var diSetupDone = false

    let onLoginSuccess = EventDispatcher("onLoginSuccess")
    let onLoginError = EventDispatcher("onLoginError")

    let loginHandler: LoginHandler

    required init(appContext: AppContext? = nil) {
        loginHandler = LoginHandler(onLoginSuccess: onLoginSuccess, onLoginError: onLoginError)
        super.init(appContext: appContext)
        clipsToBounds = true

        addSubview(onboardingViewController.view)
    }

    override func layoutSubviews() {
        onboardingViewController.view.frame = bounds
    }

    func setConfiguration(_ configuration: RNOnboardingConfiguration) {
        onboardingViewController.view.removeFromSuperview()

        onboardingViewController = OnboardingViewController(configuration: configuration.toOnboardingConfiguration())
        onboardingViewController.delegate = self

        addSubview(onboardingViewController.view)
    }

    func setConfiguration(_ configuration: RNLoginConfiguration) {
        guard !diSetupDone else {
            os_log("DI already setup, skipping...", type: .info)
            return
        }

        diSetupDone = true

        let loginConfig = configuration.toLoginConfiguration()

        SimpleResolver.sharedResolver.store(factory: Factory(type: InfomaniakNetworkLoginable.self) { _, _ in
            InfomaniakNetworkLogin(config: loginConfig)
        })
        SimpleResolver.sharedResolver.store(factory: Factory(type: InfomaniakLoginable.self) { _, _ in
            InfomaniakLogin(config: loginConfig)
        })
    }
}

extension RNInfomaniakOnboardingView: OnboardingViewControllerDelegate {
    func shouldAnimateBottomViewForIndex(_: Int) -> Bool {
        return false
    }

    func willDisplaySlideViewCell(_: InfomaniakOnboarding.SlideCollectionViewCell, at _: Int) {}

    func bottomViewForIndex(_: Int) -> (any View)? {
        return OnboardingBottomButtonsView(loginHandler: loginHandler)
    }

    func currentIndexChanged(newIndex _: Int) {}
}
