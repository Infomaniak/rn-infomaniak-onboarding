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
};

export type OnLoginSuccessEventPayload = {
    accessToken: string;
};

export type OnLoginErrorEventPayload = {
    error: string;
};

export type RNInfomaniakOnboardingViewProps = {
    //   loginConfiguration: LoginConfiguration;
    onboardingConfiguration: OnboardingConfiguration;
    onLoginSuccess?: (event: { nativeEvent: OnLoginSuccessEventPayload }) => void;
    onLoginError?: (event: { nativeEvent: OnLoginErrorEventPayload }) => void;
    style?: StyleProp<ViewStyle>;
};
