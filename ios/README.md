# iOS Module - RN Infomaniak Onboarding

This iOS module implements Infomaniak's onboarding and login system for React Native using Expo Modules.

## 📁 Project Structure

```
ios/
├── RNInfomaniakOnboarding.podspec    # CocoaPods specification with dependencies
├── RNInfomaniakOnboardingModule.swift # Expo module entry point
├── RNInfomaniakOnboardingView.swift   # Main view and login logic
├── LoginProps.swift                   # Login configuration
├── OnboardingProps.swift              # Onboarding configuration + slides
├── ConstantsEnum.swift                # App constants (bundleId, appGroup, etc.)
└── Resources/
    └── Animations/                    # Lottie animations for slides
```

## 🏗️ Architecture

### Expo Module (`RNInfomaniakOnboardingModule.swift`)

The module exposes a React Native view with two main configurations:
- `onboardingConfiguration`: Configures onboarding slides (colors, slides, animations)
- `loginConfiguration`: Configures Infomaniak login system (clientId, URLs)

**Exposed Events:**
- `onLoginSuccess`: Emitted after successful login with access token
- `onLoginError`: Emitted on login error

### Main View (`RNInfomaniakOnboardingView.swift`)

The view manages two main components:

#### 1. **OnboardingViewController**
Displays onboarding slides with:
- Background images and illustrations (static or animated)
- Text content (title + subtitle)
- Swipe navigation
- Customizable page indicator

#### 2. **LoginHandler**
Handles the entire authentication process:
- Login via `ASWebAuthenticationSession` (Safari in-app)
- Secure token retrieval and storage
- Device association with account
- 2FA support (via `DeviceAssociation`)

**Login Flow:**
```
1. loginHandler.login()
2. Open web authentication session
3. Retrieve code + verifier
4. Exchange for API token
5. Store token in Keychain
6. Attach device
7. Emit onLoginSuccess event
```

### Onboarding Configuration (`OnboardingProps.swift`)

`RNOnboardingConfiguration` structure with:
- **Colors**: `primaryColorLight/Dark`, `onPrimaryColorLight/Dark` (hex format)
- **Slides**: Array of `RNSlide`
- **Options**: `isScrollEnabled`, `isPageIndicatorHidden`

**Slide Structure (`RNSlide`):**
```swift
{
  backgroundImage: { iosAssetName: "bg1" },
  staticIllustration?: { iosAssetName: "illustration1" },
  animatedIllustration?: { fileName: "animation.json" },
  title?: "Slide title",
  subtitle?: "Slide description"
}
```

> ⚠️ A slide can have either a `staticIllustration` or an `animatedIllustration`, not both.

> ⚠️ If a slide is incorect, it'll simply ignore it

### Login Configuration (`LoginProps.swift`)

`RNLoginConfiguration` structure with:
- `clientId`: Infomaniak OAuth client ID (required)
- `loginURL`: Login service URL (default: `https://login.infomaniak.com/`)
- `redirectURIScheme`: Custom scheme for OAuth redirection

The `redirectURI` is automatically generated: `{scheme or bundleId}://oauth2redirect`

## 📦 Managing Dependencies

The `RNInfomaniakOnboarding.podspec` file uses **Swift Package Manager dependencies** via CocoaPods:

### Adding a New Dependency

#### Option 1: Classic CocoaPods Dependency
```ruby
s.dependency 'PodName', '~> 1.0'
```

#### Option 2: SPM Dependency via CocoaPods
```ruby
spm_dependency(s,
  url: 'https://github.com/org/package',
  requirement: {kind: 'upToNextMajorVersion', minimumVersion: '1.0.0'},
  products: ['ProductName']
)
```

**Available Requirement Types:**
- `upToNextMajorVersion`: `^1.0.0` (e.g., 1.0.0 to < 2.0.0)
- `upToNextMinorVersion`: `~> 1.0.0` (e.g., 1.0.0 to < 1.1.0)
- `exact`: Exact version
- `range`: Custom range

### Updating Dependencies

```bash
cd example
npx pod-install
```

## 🔧 Dependency Injection

The module uses **InfomaniakDI** for dependency injection:

```swift
// Factory declaration
let factories: [Factory] = [
    Factory(type: TokenStore.self) { _, _ in
        TokenStore()
    },
    // ...
]

// Registration
factories.forEach { SimpleResolver.sharedResolver.store(factory: $0) }

// Injection in classes
@LazyInjectService private var tokenStore: TokenStore
```

**Injected Services:**
- `TokenStore`: Token storage in Keychain
- `InfomaniakLoginable`: Login service
- `InfomaniakNetworkLoginable`: Network calls for authentication
- `DeviceManagerable`: Associated device management
- `AppGroupPathProvidable`: App Group paths
- `KeychainHelper`: Secure Keychain access

## 🎨 Resources

### Images
Images must be added to the host app's **Assets Catalog** (`example/ios/rninfomaniakonboardingexample/Images.xcassets/`).

Reference via `iosAssetName` in React Native props.

### Lottie Animations
`.json` animations must be placed in `ios/Resources/Animations/`.

The module bundle is automatically used:
```swift
Bundle(for: RNInfomaniakOnboardingModule.self)
```

## 🔐 Required Configuration

### Info.plist
// TODO

### App Groups (required for Device Manager)
Configure App Group in Xcode:
1. Target > Signing & Capabilities
2. Add "App Groups"
3. Enable the group defined in `ConstantsEnum.swift`

### Keychain Sharing (required for TokenStore)
Configure Keychain sharing in Xcode:
1. Target > Signing & Capabilities
2. Add "Keychain sharing"
3. Add all apps group
4. Add the `accessGroup` defined in `ConstantsEnum.swift`

## 🚀 Usage from React Native

``` ts
import { RNInfomaniakOnboardingView } from 'rn-infomaniak-onboarding';

<RNInfomaniakOnboardingView
  onboardingConfiguration={{
    primaryColorLight: "#FFFFFFFF",
    primaryColorDark: "#000000FF",
    slides: [
      {
        backgroundImage: { iosAssetName: "iosXcassetName" },
        animatedIllustration: { fileName: "Lottie.json" },
        title: "Welcome",
        subtitle: "Discover our app"
      }
    ]
  }}
  loginConfiguration={{
    clientId: "YOUR_CLIENT_ID",
    redirectURIScheme: "myapp.com"
  }}
  onLoginSuccess={(event) => {
    console.log('Token:', event.accessToken);
  }}
  onLoginError={(event) => {
    console.error('Error:', event.error);
  }}
/>
```

## 🐛 Debugging

### Logs
The module uses `os_log` for logging. Filter in Console.app:
```
os_log(<String>)
```

### Device Association
If encountering issues with Device Manager, check:
1. App Group is properly configured
2. `bundleId` in `ConstantsEnum.swift` matches your app
3. Device attachment logs in console

## 🔄 Module Updates

Todo every time after adding new swift depedency:
```bash
cd example
npx pod-install
```

## 📝 Important Notes

1. **Static Framework**: The module is compiled as a static framework (`s.static_framework = true`)
2. **Swift Version**: Minimum 5.10
3. **iOS Version**: Minimum 15.1
4. **Thread Safety**: `LoginHandler` uses `@MainActor` for thread safety
5. **Bundle**: Resources (animations) are loaded from the module bundle, not the main app

## 📚 References

- [Expo Modules API](https://docs.expo.dev/modules/overview/)
- [InfomaniakLogin](https://github.com/Infomaniak/ios-login)
- [InfomaniakOnboarding](https://github.com/Infomaniak/ios-onboarding)
- [InfomaniakCore](https://github.com/Infomaniak/ios-core)
