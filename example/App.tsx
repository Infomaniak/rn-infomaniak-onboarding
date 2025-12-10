import { Alert, SafeAreaView } from 'react-native';
import { LoginConfiguration, OnboardingConfiguration, RNInfomaniakOnboardingView } from 'rn-infomaniak-onboarding';

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

    let loginConfiguration: LoginConfiguration = {
        clientId: '20af5539-a4fb-421c-b45a-f43af3d90c14',
        redirectURIScheme: 'com.infomaniak.chat',
        appVersionCode: 1,
        appVersionName: '1.0.0',
    };

    return (
        <SafeAreaView style={styles.view}>
            <RNInfomaniakOnboardingView
                loginConfiguration={loginConfiguration}
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
