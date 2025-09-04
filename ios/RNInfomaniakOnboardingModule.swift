import ExpoModulesCore

public class RNInfomaniakOnboardingModule: Module {
    public func definition() -> ModuleDefinition {
        Name("RNInfomaniakOnboarding")
        View(RNInfomaniakOnboardingView.self) {}
    }
}
