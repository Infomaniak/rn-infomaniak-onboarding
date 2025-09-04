import { OnboardingConfiguration, RNInfomaniakOnboardingView } from 'rn-infomaniak-onboarding';
import { SafeAreaView, Alert } from 'react-native';

export default function App() {
    let configuration: OnboardingConfiguration = {
        primaryColorLight: '#000000ff',
        primaryColorDark: '#ffffffff',
        onPrimaryColorLight: '#FFFFFF', // Specific to Android theme
        onPrimaryColorDark: '#003547', // Specific to Android theme
        slides: [
            {
                backgroundImage: {
                    androidLightFileName: "onboarding-gradient-left-light.svg",
                    androidDarkFileName: "onboarding-gradient-left-dark.svg",
                    iosAssetName: "onboarding-background-1",
                },
                staticIllustration: {
                    androidLightFileName: "onboarding-static-illustration-light.svg",
                    androidDarkFileName: "onboarding-static-illustration-dark.svg",
                    iosAssetName: "kchat_mockup",
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
                    iosAssetName: "onboarding-background-1",
                },
                animatedIllustration: {
                    fileName: "illu_onboarding_1.json",
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
                    iosAssetName: "onboarding-background-1",
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
    };

    return (
        <SafeAreaView style={styles.view}>
            <RNInfomaniakOnboardingView
                onboardingConfiguration={configuration}
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
