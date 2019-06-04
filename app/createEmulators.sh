#!/usr/bin/env bash
#https://stackoverflow.com/a/44386974/3258117
echo no | avdmanager create avd -f -n ScreenshotEmulatorPhone -k "system-images;android-28;google_apis;x86"
cat nexus5-config.ini > ~/.android/avd/ScreenshotEmulatorPhone.avd/config.ini
emulator -avd ScreenshotEmulatorPhone

#echo no | ~/Library/Android/sdk/tools/bin/avdmanager create avd -f -n ScreenshotEmulatorPhone -k "system-images;android-28;google_apis;x86"
#echo no | ${ANDROID_HOME}/tools/bin/avdmanager create avd -f -n ScreenshotEmulatorPhone -k "system-images;android-28;google_apis;x86"