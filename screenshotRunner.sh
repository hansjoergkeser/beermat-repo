#!/bin/sh
./gradlew clean

for device in nexus5 nexus7 nexus9
do
    # https://stackoverflow.com/a/44386974/3258117
    echo no | avdmanager create avd -f -n ${device} -k "system-images;android-28;google_apis;x86"

    # Overwrite the config.ini of the just created emulator... to get the suitable nexus skin with resolution, etc.
    cat ${device}-config.ini > ~/.android/avd/${device}.avd/config.ini

    # if avdmanager is not known, you need to set it as environment variable... or you could try this instead:
    # echo no | ~/Library/Android/sdk/tools/bin/avdmanager create avd -f -n ${device} -k "system-images;android-28;google_apis;x86"
    # echo no | ${ANDROID_HOME}/tools/bin/avdmanager create avd -f -n ${device} -k "system-images;android-28;google_apis;x86"

    # Start the emulator
    emulator -avd ${device} &
    adb wait-for-device

    sleep 10

    # https://spin.atomicobject.com/2016/03/10/android-test-script/
    WAIT_CMD="adb wait-for-device shell getprop init.svc.bootanim"
    until ${WAIT_CMD} | grep -m 1 stopped; do
        echo "Waiting for emulator..."
        sleep 2
    done

    # the Android OS still needs time, otherwise the following demo mode commands won't work
    sleep 20
    echo "Emulator is ready now."

    # Start demo mode
    adb shell settings put global sysui_demo_allowed 1
    # Display time 12:00
    adb shell am broadcast -a com.android.systemui.demo -e command clock -e hhmm 1200
    # Display full mobile data without type
    adb shell am broadcast -a com.android.systemui.demo -e command network -e mobile show -e level 4 -e datatype false
    adb shell am broadcast -a com.android.systemui.demo -e command network -e wifi show -e level 4 -e fully true
    # Hide notifications
    adb shell am broadcast -a com.android.systemui.demo -e command notifications -e visible false
    # Show full battery but not in charging state
    adb shell am broadcast -a com.android.systemui.demo -e command battery -e plugged false -e level 100

    # Start tests
    ./gradlew cAT -Pandroid.testInstrumentationRunnerArguments.class=de.hajo.beermat.screenshots.MainActivityScreenshotEspTests

    # Disable demo mode
    adb shell am broadcast -a com.android.systemui.demo -e command exit

    # https://gist.github.com/royclarkson/f39115fe0a7ac1f08630
    adb devices | grep "emulator-" | while read -r emulator device; do
        adb -s ${emulator} emu kill
    done

    # Remove the emulator
    avdmanager delete avd -n ${device}
done