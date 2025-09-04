import type { StyleProp, ViewStyle } from 'react-native';

export type Slide = {
  backgroundImageName?: string;
  backgroundImageTintColor?: string; // Color mapped from string
  illustrationName?: string;
  animationName?: string;
  title?: string; // defaults to ""
  subtitle?: string; // defaults to ""
};

export type OnboardingConfiguration = {
  headerImageName?: string;
  slides?: Slide[];
  pageIndicatorColor?: string; // Color mapped from string
  isScrollEnabled?: boolean; // defaults to true
  isPageIndicatorHidden?: boolean; // defaults to false
};

export type RNInfomaniakOnboardingViewProps = {
  onboardingConfiguration: OnboardingConfiguration;
  style?: StyleProp<ViewStyle>;
};
