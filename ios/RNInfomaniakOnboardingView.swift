import ExpoModulesCore
import InfomaniakOnboarding
import SwiftUI

class RNInfomaniakOnboardingView: ExpoView {
    let onboardingViewController = OnboardingViewController(configuration: .init(headerImage: nil,
                                                                                 slides: [
                                                                                     .init(
                                                                                         backgroundImage: UIImage(
                                                                                             systemName: "circle"
                                                                                         )!,
                                                                                         backgroundImageTintColor: nil,
                                                                                         content: .illustration(
                                                                                             UIImage(systemName: "circle")!
                                                                                         ),
                                                                                         bottomView: Text("Hello")
                                                                                     )
                                                                                 ],
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
}
