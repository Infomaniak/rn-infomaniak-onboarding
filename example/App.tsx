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
          createAccountUrl: "https://welcome.infomaniak.com/signup/ikmail?app=true",
        }}
        onboardingConfiguration={{
          primaryColorLight: '#0088B2',
          primaryColorDark: '#8DCFF1',
          onPrimaryColorLight: '#FFFFFF', // Specific to Android theme
          onPrimaryColorDark: '#003547', // Specific to Android theme
          slides: [
            {
              backgroundImage: {
                androidLightFileName: "onboarding-gradient-left-light.svg",
                androidDarkFileName: "onboarding-gradient-left-dark.svg",
                iosAssetName: "onboarding-gradient-left",
              },
              animatedIllustration: {
                fileName: "onboarding-animation-1.lottie",
                lightThemeName: undefined,
                darkThemeName: "Pink-Dark",
              },
              title: 'Slide 1',
              subtitle: 'Subtitle 1',
            },
            {
              backgroundImage: {
                androidLightFileName: "onboarding-gradient-right-light.svg",
                androidDarkFileName: "onboarding-gradient-right-dark.svg",
                iosAssetName: "onboarding-gradient-right",
              },
              animatedIllustration: {
                fileName: "onboarding-animation-2.lottie",
                lightThemeName: undefined,
                darkThemeName: "Pink-Dark",
              },
              title: 'Slide 2',
              subtitle: 'Subtitle 2',
            },
            {
              backgroundImage: {
                androidLightFileName: "onboarding-gradient-left-light.svg",
                androidDarkFileName: "onboarding-gradient-left-dark.svg",
                iosAssetName: "onboarding-gradient-left",
              },
              staticIllustration: {
                androidLightFileName: "onboarding-static-illustration-light.svg",
                androidDarkFileName: "onboarding-static-illustration-dark.svg",
                iosAssetName: "onboarding-static-illustration",
              },
              title: 'Slide 3',
              subtitle: 'Subtitle 3',
            },
          ],
        }}
        onLoginSuccess={(event) => {
          Alert.alert('Login Success', event.nativeEvent.accessToken);
        }}
        onLoginError={(event) => {
          console.error(`Login Error: ${event.nativeEvent.error}`);
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
