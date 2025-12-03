import { RNInfomaniakOnboardingView, OnboardingConfiguration } from 'rn-infomaniak-onboarding';
import { SafeAreaView, Alert } from 'react-native';

export default function App() {
    const onboardingConfig: OnboardingConfiguration = {
        primaryColorLight: '#3F51B5',
        primaryColorDark: '#5C6BC0',
        onPrimaryColorLight: '#FFFFFF',
        onPrimaryColorDark: '#000000ff',
        slides: [
            {
                backgroundImageNameLight: 'onboarding-background-1',
                backgroundImageNameDark: 'onboarding-background-1',
                illustrationName: 'kchat_mockup',
                illustrationLightThemeName: 'kchat_mockup_light',
                illustrationDarkThemeName: 'kchat_mockup_dark',
                title: 'Welcome',
                subtitle: 'Discover our app',
            },
            {
                backgroundImageNameLight: 'onboarding-background-1',
                backgroundImageNameDark: 'onboarding-background-1',
                illustrationName: 'kchat_mockup',
                illustrationLightThemeName: 'kchat_mockup_light',
                illustrationDarkThemeName: 'kchat_mockup_dark',
                title: 'Features',
                subtitle: 'All the features you need',
            },
            {
                backgroundImageNameLight: 'onboarding-background-1',
                backgroundImageNameDark: 'onboarding-background-1',
                illustrationName: 'kchat_mockup',
                title: 'Get Started',
                subtitle: 'Let\'s go!',
            },
        ],
    };

    return (
        <SafeAreaView style={styles.container}>
            <RNInfomaniakOnboardingView
                style={styles.view}
                onboardingConfiguration={onboardingConfig}
                onLoginSuccess={(event) => {
                    Alert.alert('Login réussi', `Token: ${event.nativeEvent.accessToken}`);
                }}
                onLoginError={(event) => {
                    Alert.alert('Erreur de login', event.nativeEvent.error);
                }}
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