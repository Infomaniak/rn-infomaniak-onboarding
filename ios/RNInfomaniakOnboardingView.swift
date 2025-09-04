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

import ExpoModulesCore
import InfomaniakOnboarding
import SwiftUI

class RNInfomaniakOnboardingView: ExpoView {
    private var onboardingViewController = OnboardingViewController(configuration: .init(headerImage: nil,
                                                                                         slides: [],
                                                                                         pageIndicatorColor: nil,
                                                                                         isScrollEnabled: true,
                                                                                         dismissHandler: nil,
                                                                                         isPageIndicatorHidden: false))
    let onLoad = EventDispatcher()

    required init(appContext: AppContext? = nil) {
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
        addSubview(onboardingViewController.view)
    }
}
