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
    var primaryColorLight: String = "#000000"

    @Field
    var primaryColorDark: String = "#FFFFFF"

    @Field
    var onPrimaryColorLight: String = "#FFFFFF"

    @Field
    var onPrimaryColorDark: String = "#000000"

    @Field
    var slides: [RNSlide] = []

    @Field
    var isScrollEnabled = true

    @Field
    var isPageIndicatorHidden = false

    @MainActor
    func toOnboardingConfiguration() -> OnboardingConfiguration {
        OnboardingConfiguration(
            headerImage: nil,
            slides: slides.compactMap { $0.toSlide() },
            pageIndicatorColor: colorFromHex(primaryColorLight),
            isScrollEnabled: isScrollEnabled,
            dismissHandler: nil,
            isPageIndicatorHidden: isPageIndicatorHidden
        )
    }
}

struct RNSlide: Record {
    @Field
    var backgroundImageNameLight: String? = nil

    @Field
    var backgroundImageNameDark: String? = nil

    @Field
    var illustrationName: String? = nil

    @Field
    var illustrationLightThemeName: String? = nil

    @Field
    var illustrationDarkThemeName: String? = nil

    @Field
    var animationName: String? = nil

    @Field
    var title: String? = nil

    @Field
    var subtitle: String? = nil

    @MainActor
    func toSlide() -> Slide? {
        // Use light theme image for now (could be improved with trait collection)
        let backgroundImageName = backgroundImageNameLight ?? backgroundImageNameDark
        guard let backgroundImage = imageForName(backgroundImageName) else {
            return nil
        }

        let bottomView = VStack {
            if let title {
                Text(title)
                    .font(.title)
                    .bold()
                    .padding(.bottom, 8)
            }
            if let subtitle {
                Text(subtitle)
                    .font(.body)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 24)
            }
        }

        if let illustration = imageForName(illustrationName) {
            return Slide(
                backgroundImage: backgroundImage,
                backgroundImageTintColor: nil,
                content: .illustration(illustration),
                bottomView: bottomView
            )
        } else if let animationName {
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

private func colorFromHex(_ hex: String) -> UIColor {
    var hexSanitized = hex.trimmingCharacters(in: .whitespacesAndNewlines)
    hexSanitized = hexSanitized.replacingOccurrences(of: "#", with: "")

    var rgb: UInt64 = 0
    Scanner(string: hexSanitized).scanHexInt64(&rgb)

    let length = hexSanitized.count
    let r, g, b, a: CGFloat

    if length == 6 {
        r = CGFloat((rgb & 0xFF0000) >> 16) / 255.0
        g = CGFloat((rgb & 0x00FF00) >> 8) / 255.0
        b = CGFloat(rgb & 0x0000FF) / 255.0
        a = 1.0
    } else if length == 8 {
        r = CGFloat((rgb & 0xFF00_0000) >> 24) / 255.0
        g = CGFloat((rgb & 0x00FF_0000) >> 16) / 255.0
        b = CGFloat((rgb & 0x0000_FF00) >> 8) / 255.0
        a = CGFloat(rgb & 0x0000_00FF) / 255.0
    } else {
        return .black
    }

    return UIColor(red: r, green: g, blue: b, alpha: a)
}
