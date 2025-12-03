# Onboarding library

## How to run on Android 🤖

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

## How to run on iOS 🍏

1. Install Node.js
   https://nodejs.org/en/download
2. Install dependencies from the root of the project
   ```sh
   npm install
   ```
3. Install cocoapods (https://guides.cocoapods.org/using/getting-started.html):
	```sh
	gem install cocoapods
	```
4. Install setup example:
	```sh
	cd example
	npm install
	npx pod-install
	```
5. Start the Expo development server (keep this terminal open) (in the example folder):
	```sh
	npm start
	```
6. Open the XCWorkspace in xCode
	```sh
	open ios/rninfomaniakonboardingexample.xcworkspace
	```
7. In Xcode, run the example app as a regular iOS app 🎉

> [!IMPORTANT]
> Whatever you do, do not use "Expo Go"

## Troubleshooting

* If you use Proxyman, make sure it's correctly setup because if the api calls do not work they app won't be able to run correctly
  at all when you try to build it on the emulator.  
