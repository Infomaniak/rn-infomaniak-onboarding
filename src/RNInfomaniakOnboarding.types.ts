import type { StyleProp, ViewStyle } from 'react-native';

export type Slide = {
  backgroundImageNameLight: string;
  backgroundImageNameDark: string;
  illustrationName: string;
  illustrationLightThemeName?: string;
  illustrationDarkThemeName?: string;
  title?: string; // defaults to ""
  subtitle?: string; // defaults to ""
};

export type OnboardingConfiguration = {
  primaryColorLight: string; // Color mapped from string
  primaryColorDark: string; // Color mapped from string
  onPrimaryColorLight: string; // Color mapped from string, specific to Android theme
  onPrimaryColorDark: string; // Color mapped from string, specific to Android theme
  slides?: Slide[];
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
