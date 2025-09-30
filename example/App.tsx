import { RNInfomaniakOnboardingView } from 'rn-infomaniak-onboarding';
import { SafeAreaView, Alert } from 'react-native';

export default function App() {
  return (
    <SafeAreaView style={styles.view}>
      <RNInfomaniakOnboardingView
        loginConfiguration={{
          clientId: '20af5539-a4fb-421c-b45a-f43af3d90c14',
          redirectURI: 'com.infomaniak.chat://oauth2redirect'
        }}
        onboardingConfiguration={{
          headerImageName: 'logo',
          slides: [
            {
              backgroundImageName: 'onboarding-background-1',
              illustrationName: 'kchat_mockup',
              title: 'Slide 1',
              subtitle: 'Subtitle 1',
            },
            {
              backgroundImageName: 'onboarding-background-1',
              illustrationName: 'kchat_mockup',
              title: 'Slide 2',
              subtitle: 'Subtitle 2',
            },
          ],
          pageIndicatorColor: '#ff0000ff',
          isScrollEnabled: true,
          isPageIndicatorHidden: false,
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
