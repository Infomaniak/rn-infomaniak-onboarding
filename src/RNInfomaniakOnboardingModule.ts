import { NativeModule, requireNativeModule } from 'expo';

declare class RNInfomaniakOnboardingModule extends NativeModule {
}

// This call loads the native module object from the JSI.
export default requireNativeModule<RNInfomaniakOnboardingModule>('RNInfomaniakOnboarding');
