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

export type LoginConfiguration = {
  clientId: string;
  loginURL?: string; // defaults to "https://login.infomaniak.com/"
  redirectURI?: string; // defaults to "appbundleid://oauth2redirect"
};

export type OnLoginSuccessEventPayload = {
  accessToken: string;
};

export type OnLoginErrorEventPayload = {
  error: string;
};

export type RNInfomaniakOnboardingViewProps = {
  loginConfiguration: LoginConfiguration;
  onboardingConfiguration: OnboardingConfiguration;
  onLoginSuccess?: (event: { nativeEvent: OnLoginSuccessEventPayload }) => void;
  onLoginError?: (event: { nativeEvent: OnLoginErrorEventPayload }) => void;
  style?: StyleProp<ViewStyle>;
};
