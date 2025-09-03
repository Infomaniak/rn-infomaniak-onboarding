import * as React from 'react';

import { RNInfomaniakOnboardingViewProps } from './RNInfomaniakOnboarding.types';

export default function RNInfomaniakOnboardingView(props: RNInfomaniakOnboardingViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
