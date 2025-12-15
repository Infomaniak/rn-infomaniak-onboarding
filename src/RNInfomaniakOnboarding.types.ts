import type { StyleProp, ViewStyle } from 'react-native';

export type Slide = {
  backgroundImage: StaticIllustration;
  staticIllustration?: StaticIllustration;
  animatedIllustration?: AnimatedIllustration;
  title?: string; // defaults to ""
  subtitle?: string; // defaults to ""
};

export type StaticIllustration = {
    androidLightFileName: string;
    androidDarkFileName: string;
    iosAssetName: string;
};

export type AnimatedIllustration = {
    fileName: string;
    lightThemeName?: string;
    darkThemeName?: string;
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
  redirectURIScheme: string; // The "appbundleid" part in the redirect URI of the form "appbundleid://oauth2redirect"
  appVersionCode: number; // The Android build number. For api call headers and the user agent
  appVersionName: string; // App version like "2.14.8". For api call headers and the user agent
  createAccountUrl: string; // Url to create the account specific to this app. E.g. https://welcome.infomaniak.com/signup/...
  successHost?: string; // Url to detect that the login has finished with success. By default "ksuite.infomaniak.com"
  cancelHost?: string; // Url to detect that the login has finished with an error. By default "welcome.infomaniak.com"
  allowMultipleAccounts: boolean; // If the cross app login should limit to only one account or allow the selection of any amount of accounts
};

export type OnLoginSuccessEventPayload = {
  accessTokens: string[];
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
