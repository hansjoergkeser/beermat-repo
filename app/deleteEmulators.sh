#!/usr/bin/env bash
#https://gist.github.com/royclarkson/f39115fe0a7ac1f08630
adb devices | grep "emulator-" | while read -r emulator device; do
  adb -s ${emulator} emu kill
done
avdmanager delete avd -n ScreenshotEmulatorPhone

#~/Library/Android/sdk/tools/bin/avdmanager delete avd -n ScreenshotEmulatorPhone
#${ANDROID_HOME}/tools/bin/avdmanager delete avd -n ScreenshotEmulatorPhone