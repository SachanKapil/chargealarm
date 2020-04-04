package com.chargealarm.data

import com.chargealarm.constant.AppConstants
import com.chargealarm.data.preferences.PreferenceManager

object DataManager {

    fun saveBatteryPercentage(percentage: Int) {
        PreferenceManager.putInt(
            AppConstants.PreferenceConstants.BATTERY_PERCENTAGE_FOR_ALARM,
            percentage
        )
    }

    fun getBatteryPercentage(): Int {
        return PreferenceManager.getInt(
            AppConstants.PreferenceConstants.BATTERY_PERCENTAGE_FOR_ALARM
        )
    }

    fun clearPreferences() {
        PreferenceManager.clearAllPrefs()
    }

}