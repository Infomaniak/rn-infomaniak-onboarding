import { RNInfomaniakOnboardingView } from 'rn-infomaniak-onboarding';
import { SafeAreaView, Alert } from 'react-native';

export default function App() {
  return (
    <SafeAreaView style={styles.view}>
      <RNInfomaniakOnboardingView
        loginConfiguration={{
          clientId: '20af5539-a4fb-421c-b45a-f43af3d90c14',
          redirectURIScheme: 'com.infomaniak.chat',
          appVersionCode: 18,
          appVersionName: '0.2.5',
        }}
        onboardingConfiguration={{
          primaryColorLight: '#0088B2',
          primaryColorDark: '#8DCFF1',
          onPrimaryColorLight: '#FFFFFF', // Specific to Android theme
          onPrimaryColorDark: '#003547', // Specific to Android theme
          slides: [
            {
              backgroundImageNameLight: 'onboarding-gradient-left-light.svg',
              backgroundImageNameDark: 'onboarding-gradient-left-dark.svg',
              illustrationName: 'onboarding-animation-1.lottie',
              illustrationLightThemeName: undefined,
              illustrationDarkThemeName: "Pink-Dark",
              title: 'Slide 1',
              subtitle: 'Subtitle 1',
            },
            {
              backgroundImageNameLight: 'onboarding-gradient-right-light.svg',
              backgroundImageNameDark: 'onboarding-gradient-right-dark.svg',
              illustrationName: 'onboarding-animation-2.lottie',
              illustrationLightThemeName: undefined,
              illustrationDarkThemeName: "Pink-Dark",
              title: 'Slide 2',
              subtitle: 'Subtitle 2',
            },
          ],
        }}
        onLoginSuccess={(event) => {
          Alert.alert('Login Success', event.nativeEvent.accessToken);
        }}
        onLoginError={(event) => {
          Alert.alert('Login Error', event.nativeEvent.error);
        }}
        style={styles.view}
      />
    </SafeAreaView>
  );
}

const styles = {
  container: {
    flex: 1,
    backgroundColor: '#eee',
  },
  view: {
    flex: 1,
  }
};
