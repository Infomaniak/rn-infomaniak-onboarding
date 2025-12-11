import Foundation

public enum Constants {
    public static let bundleId = "com.infomaniak.rnkchat"

    public static let appName = Bundle.main.object(forInfoDictionaryKey: "CFBundleName") as! String? ?? "RnkChat"
    public static let appGroupIdentifier = "group.\(Constants.bundleId)"
    public static let sharedAppGroupName = "group.com.infomaniak"

    private static let appIdentifierPrefix = Bundle.main.infoDictionary!["AppIdentifierPrefix"] as! String
    public static let accessGroup: String = Constants.appIdentifierPrefix + Constants.bundleId
}
