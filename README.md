# Onboarding library

## How to run on Android

1. Install Node.js
2. Install dependencies from the root of the project
   ```bash
   npm install
   ```
3. From the root of the git project, open Android Studio via terminal so it inherits your Node install:
   ```bash
   open -a "Android Studio" example/android
   ```
4. Navigate to the example app:
   ```
   cd example
   ```
5. Start the Expo development server (keep this terminal open):
   ```
   npx expo start
   ```
6. In Android Studio, run the example app as a regular Android project.

> [!IMPORTANT]
> Whatever you do, do not use "Expo Go"
