import { NativeModule, requireNativeModule } from 'expo';

import { RNInfomaniakOnboardingModuleEvents } from './RNInfomaniakOnboarding.types';

declare class RNInfomaniakOnboardingModule extends NativeModule<RNInfomaniakOnboardingModuleEvents> {
  PI: number;
  hello(): string;
  setValueAsync(value: string): Promise<void>;
}

// This call loads the native module object from the JSI.
export default requireNativeModule<RNInfomaniakOnboardingModule>('RNInfomaniakOnboarding');
