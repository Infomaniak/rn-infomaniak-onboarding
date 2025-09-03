import { requireNativeView } from 'expo';
import * as React from 'react';

import { RNInfomaniakOnboardingViewProps } from './RNInfomaniakOnboarding.types';

const NativeView: React.ComponentType<RNInfomaniakOnboardingViewProps> =
  requireNativeView('RNInfomaniakOnboarding');

export default function RNInfomaniakOnboardingView(props: RNInfomaniakOnboardingViewProps) {
  return <NativeView {...props} />;
}
