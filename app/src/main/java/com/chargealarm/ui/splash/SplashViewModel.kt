package com.chargealarm.ui.splash

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chargealarm.constant.AppConstants

class SplashViewModel : ViewModel() {
    private val splashTimeOut: Long = 2000
    private val splashLiveData = MutableLiveData<String>()
    internal fun getSplashLiveData(): MutableLiveData<String> {
        return splashLiveData
    }

    // Method to start new activity after 1 second
    fun showSplashScreen() {
        Handler().postDelayed({
            splashLiveData.value = AppConstants.SplashConstants.OPEN_HOME_SCREEN
        }, splashTimeOut)
    }

}