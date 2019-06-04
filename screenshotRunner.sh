#!/usr/bin/env bash
#https://stackoverflow.com/a/44386974/3258117
echo no | avdmanager create avd -f -n ScreenshotEmulatorPhone -k "system-images;android-28;google_apis;x86"
cat app/nexus5-config.ini > ~/.android/avd/ScreenshotEmulatorPhone.avd/config.ini

#echo no | ~/Library/Android/sdk/tools/bin/avdmanager create avd -f -n ScreenshotEmulatorPhone -k "system-images;android-28;google_apis;x86"
#echo no | ${ANDROID_HOME}/tools/bin/avdmanager create avd -f -n ScreenshotEmulatorPhone -k "system-images;android-28;google_apis;x86"

#https://spin.atomicobject.com/2016/03/10/android-test-script/
WAIT_CMD="adb wait-for-device shell getprop init.svc.bootanim"
emulator -avd ScreenshotEmulatorPhone &
until $WAIT_CMD | grep -m 1 stopped; do
  echo "Waiting for emulator..."
  sleep 1
done
printf "\nEmulator is ready now."

./gradlew clean cAT -Pandroid.testInstrumentationRunnerArguments.class=de.hajo.beermat.screenshots.MainActivityScreenshotEspTests -i

#https://gist.github.com/royclarkson/f39115fe0a7ac1f08630
adb devices | grep "emulator-" | while read -r emulator device; do
  adb -s ${emulator} emu kill
done
avdmanager delete avd -n ScreenshotEmulatorPhone