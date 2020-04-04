package com.chargealarm.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chargealarm.R
import com.chargealarm.base.BaseActivity
import com.chargealarm.constant.AppConstants
import com.chargealarm.ui.home.HomeActivity
import com.chargealarm.util.ResourceUtil

class SplashActivity : BaseActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        initObservers()
        splashViewModel.showSplashScreen()
    }

    private fun initObservers() {
        splashViewModel.getSplashLiveData().observe(this, Observer {
            when (it) {
                AppConstants.SplashConstants.OPEN_HOME_SCREEN -> openHomeActivity()
            }
        })
    }

    private fun openHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAfterTransition()
    }

}
