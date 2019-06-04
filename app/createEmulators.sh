#!/usr/bin/env bash
echo no | ${ANDROID_HOME}/tools/bin/avdmanager create avd -f -n ScreenshotEmulatorPhone -k "system-images;android-28;google_apis;x86"
cat nexus5-config.ini > ~/.android/avd/ScreenshotEmulatorPhone.avd/config.ini
emulator -avd ScreenshotEmulatorPhone