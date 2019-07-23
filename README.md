# Shopaholic SDK

#### A coding challenge completed for Entersekt

## Quick start

Clone this repository and open it in Android Studio. It's shipped with a demo app that shows how to use the SDK. 

To run the demo app, select "Run Demo App" build configuration and click on the run button.

## Building SDK artifact

To build an AAR artifact file to use the SDK in another project, select the "Assemble SDK Artifact" build configuration and click on the run button.

The .aar file will be created in the `{project-base-dir}/shopaholicsdk/build/outputs/aar` directory.

### Network security configuration

To use the Shopaholic SDK in a project you need to configure a network security configuration. This is for devices running Android 9 or above.

Add a file called `network_security_configuration.xml` in the `res/xml` folder of your app. The file should have the following contents:

```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">www.mocky.io</domain>
    </domain-config>
</network-security-config>
```

After creating the file above, add the following line to your app's Manifest file:

```
<?xml version="1.0" encoding="utf-8"?>
<manifest ...>
    <uses-permission android:name="android.permission.INTERNET" />
    <application
        ...
        android:networkSecurityConfig="@xml/network_security_config"
        ...>
        ...
    </application>
</manifest>
```