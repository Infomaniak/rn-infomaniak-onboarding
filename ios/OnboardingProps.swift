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
import Foundation
import InfomaniakOnboarding
import SwiftUI

struct RNOnboardingConfiguration: Record {
    @Field
    var headerImageName: String? = nil

    @Field
    var slides: [RNSlide] = []

    @Field
    var pageIndicatorColor: UIColor? = nil

    @Field
    var isScrollEnabled = true

    @Field
    var isPageIndicatorHidden = false

    @MainActor
    func toOnboardingConfiguration() -> OnboardingConfiguration {
        OnboardingConfiguration(
            headerImage: imageForName(headerImageName),
            slides: slides.compactMap { $0.toSlide() },
            pageIndicatorColor: pageIndicatorColor,
            isScrollEnabled: isScrollEnabled,
            dismissHandler: nil,
            isPageIndicatorHidden: isPageIndicatorHidden
        )
    }
}

struct RNSlide: Record {
    @Field
    var backgroundImageName: String? = nil

    @Field
    var backgroundImageTintColor: UIColor? = nil

    @Field
    var illustrationName: String? = nil

    @Field
    var animationName: String? = nil

    @Field
    var title = ""

    @Field
    var subtitle = ""

    @MainActor
    func toSlide() -> Slide? {
        guard let backgroundImage = imageForName(backgroundImageName) else {
            return nil
        }

        let bottomView = VStack {
            Text(title)
                .font(.title)
                .bold()
                .padding(.bottom, 8)
            Text(subtitle)
                .font(.body)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 24)
        }

        if let illustration = imageForName(illustrationName) {
            return Slide(
                backgroundImage: backgroundImage,
                backgroundImageTintColor: backgroundImageTintColor,
                content: .illustration(illustration),
                bottomView: bottomView
            )
        } else if animationName != nil {
            fatalError("Not implemented yet")
        } else {
            return nil
        }
    }
}

private func imageForName(_ name: String?) -> UIImage? {
    guard let name, !name.isEmpty else {
        return nil
    }
    return UIImage(named: name, in: Bundle.main, compatibleWith: nil)
}
