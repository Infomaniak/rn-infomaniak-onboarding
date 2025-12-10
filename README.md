# Onboarding library

## How to use this library in an Expo module

This library provides a RNInfomaniakOnboardingView component that displays the onboarding screens and manages the entire login
flow. After a successful login, the view returns an access token.

### Basic usage
```tsx
<RNInfomaniakOnboardingView
    loginConfiguration={{
    ...
    }}
    onboardingConfiguration={{
    ...
    }}
    onLoginSuccess={(accessToken) => console.log(event.nativeEvent.accessToken)}
    onLoginError={(event) => console.error(event.nativeEvent.error)}
/>
```

### Configuration

Check the example app: [example/App.tsx](example/App.tsx)

See all available options: [src/RNInfomaniakOnboarding.types.ts](src/RNInfomaniakOnboarding.types.ts)

## Developing the library on Android

1. Install Node.js
   https://nodejs.org/en/download
2. Install dependencies from the root of the project
   ```bash
   npm install
   ```
3. Make sure your Android Studio is closed before doing this. From the root of the git project, open Android Studio via terminal
   so it inherits your Node install:
   ```bash
   open -a "Android Studio" example/android
   ```
4. Navigate to the example app:
   ```
   cd example
   ```
5. Install dependencies from the root of this example project
   ```bash
   npm install
   ```
6. Start the Expo development server (keep this terminal open):
   ```
   npx expo start
   ```
7. In Android Studio, run the example app as a regular Android project.

> [!IMPORTANT]
> Whatever you do, do not use "Expo Go"

## Troubleshooting

* If you use Proxyman, make sure it's correctly setup because if the api calls do not work they app won't be able to run correctly
  at all when you try to build it on the emulator.  
