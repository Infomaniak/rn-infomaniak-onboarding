const { withEntitlementsPlist, withInfoPlist } = require('@expo/config-plugins');

/**
 * @param {import('@expo/config-plugins').ExpoConfig} config
 * @param {Object} props - Options de configuration
 * @param {string} props.appGroupIdentifier - L'identifiant du App Group (ex: "group.com.infomaniak.kchat")
 * @param {string} props.keychainAccessGroup - L'identifiant du Keychain Access Group
 */
const withInfomaniakOnboarding = (config, props = {}) => {
    const { appGroupIdentifier, keychainAccessGroup } = props;

    if (!appGroupIdentifier) {
        throw new Error('appGroupIdentifier is required for rn-infomaniak-onboarding');
    }

    // Ajouter les entitlements nécessaires
    config = withEntitlementsPlist(config, (config) => {
        // App Groups
        if (!config.modResults['com.apple.security.application-groups']) {
            config.modResults['com.apple.security.application-groups'] = [];
        }
        if (!config.modResults['com.apple.security.application-groups'].includes(appGroupIdentifier)) {
            config.modResults['com.apple.security.application-groups'].push(appGroupIdentifier);
        }

        // Keychain Access Groups
        if (keychainAccessGroup) {
            if (!config.modResults['keychain-access-groups']) {
                config.modResults['keychain-access-groups'] = [];
            }
            const keychainGroup = `$(AppIdentifierPrefix)${keychainAccessGroup}`;
            if (!config.modResults['keychain-access-groups'].includes(keychainGroup)) {
                config.modResults['keychain-access-groups'].push(keychainGroup);
            }
        }

        return config;
    });

    return config;
};

module.exports = withInfomaniakOnboarding;