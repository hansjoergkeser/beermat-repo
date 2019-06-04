#!/usr/bin/env bash
function start_clean_status_bar {
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
}

start_clean_status_bar