import { registerWebModule, NativeModule } from 'expo';

import { RNInfomaniakOnboardingModuleEvents } from './RNInfomaniakOnboarding.types';

class RNInfomaniakOnboardingModule extends NativeModule<RNInfomaniakOnboardingModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(RNInfomaniakOnboardingModule, 'RNInfomaniakOnboardingModule');
