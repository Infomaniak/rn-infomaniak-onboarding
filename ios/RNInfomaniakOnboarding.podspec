require 'json'

package = JSON.parse(File.read(File.join(__dir__, '..', 'package.json')))

Pod::Spec.new do |s|
  s.name           = 'RNInfomaniakOnboarding'
  s.version        = package['version']
  s.summary        = package['description']
  s.description    = package['description']
  s.license        = package['license']
  s.author         = package['author']
  s.homepage       = package['homepage']
  s.platforms      = {
    :ios => '15.1',
    :tvos => '15.1'
  }
  s.swift_version  = '5.10'
  s.source         = { git: 'https://github.com/Infomaniak/rn-infomaniak-onboarding' }
  s.static_framework = true

  s.dependency 'ExpoModulesCore'
  spm_dependency(s,
    url: 'https://github.com/Infomaniak/ios-onboarding',
    requirement: {kind: 'upToNextMajorVersion', minimumVersion: '1.3.0'},
    products: ['InfomaniakOnboarding']
  )
  spm_dependency(s,
    url: 'https://github.com/Infomaniak/ios-login',
    requirement: {kind: 'upToNextMajorVersion', minimumVersion: '7.3.1'},
    products: ['InfomaniakLogin']
  )
  spm_dependency(s,
    url: 'https://github.com/Infomaniak/ios-features',
    requirement: {kind: 'upToNextMajorVersion', minimumVersion: '5.0.0'},
    products: ['InterAppLogin']
  )
  # Swift/Objective-C compatibility
  s.pod_target_xcconfig = {
    'DEFINES_MODULE' => 'YES',
  }

  s.source_files = "**/*.{h,m,mm,swift,hpp,cpp}"
end
