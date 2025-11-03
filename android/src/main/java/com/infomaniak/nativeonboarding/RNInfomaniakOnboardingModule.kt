package com.infomaniak.nativeonboarding

import com.infomaniak.nativeonboarding.models.LoginConfiguration
import com.infomaniak.nativeonboarding.models.OnboardingConfiguration
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class RNInfomaniakOnboardingModule : Module() {
    // Each module class must implement the definition function. The definition consists of components
    // that describes the module's functionality and behavior.
    // See https://docs.expo.dev/modules/module-api for more details about available components.
    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('RNInfomaniakOnboarding')` in JavaScript.
        Name("RNInfomaniakOnboarding")

        // Enables the module to be used as a native view. Definition components that are accepted as part of
        // the view definition: Prop, Events.
        View(RNInfomaniakOnboardingView::class) {
            // Defines a setter for the `loginConfiguration` prop.
            Prop("loginConfiguration") { view: RNInfomaniakOnboardingView, config: LoginConfiguration? ->
                view.setLoginConfig(config)
            }

            // Defines a setter for the `onboardingConfiguration` prop.
            Prop("onboardingConfiguration") { view: RNInfomaniakOnboardingView, config: OnboardingConfiguration? ->
                config?.let { view.setOnboardingConfig(it) }
            }

            // Defines events that the view can send to JavaScript.
            Events("onLoginSuccess", "onLoginError")
        }
    }
}
