import { RNInfomaniakOnboardingView } from 'rn-infomaniak-onboarding';
import { SafeAreaView } from 'react-native';

export default function App() {
  return (
    <SafeAreaView style={styles.view}>
      <RNInfomaniakOnboardingView
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
