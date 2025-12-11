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
import InfomaniakOnboarding
import OSLog
import Sentry
import SwiftUI

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

    private let sentryService = SentryService()

    let onLoginSuccess = EventDispatcher("onLoginSuccess")
    let onLoginError = EventDispatcher("onLoginError")

    let loginHandler: LoginHandler

    required init(appContext: AppContext? = nil) {
        let factories: [Factory] = [
            Factory(type: AppGroupPathProvidable.self) { _, _ in
                guard let provider = AppGroupPathProvider(
                    realmRootPath: "kchat",
                    appGroupIdentifier: Constants.appGroupIdentifier
                ) else {
                    fatalError("could not safely init AppGroupPathProvider")
                }

                return provider
            },
            Factory(type: DeviceManagerable.self) { _, _ in
                let version = Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as! String? ?? "x.x"
                return DeviceManager(appGroupIdentifier: Constants.sharedAppGroupName,
                                     appMarketingVersion: version,
                                     capabilities: [.twoFactorAuthenticationChallengeApproval])
            },
            Factory(type: KeychainHelper.self) { _, _ in
                KeychainHelper(accessGroup: Constants.accessGroup)
            },
            Factory(type: TokenStore.self) { _, _ in
                TokenStore()
            },
        ]

        factories.forEach { SimpleResolver.sharedResolver.store(factory: $0) }

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
        let factories: [Factory] = [
            Factory(type: InfomaniakNetworkLoginable.self) { _, _ in
                InfomaniakNetworkLogin(config: loginConfig)
            },
            Factory(type: InfomaniakLoginable.self) { _, _ in
                InfomaniakLogin(config: loginConfig)
            },
        ]

        factories.forEach { SimpleResolver.sharedResolver.store(factory: $0) }
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
