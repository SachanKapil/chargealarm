package com.chargealarm.ui.home

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.chargealarm.R
import com.chargealarm.base.BaseActivity
import com.chargealarm.data.DataManager
import com.chargealarm.receiver.PowerConnectionReceiver
import com.chargealarm.service.ChargingService
import com.chargealarm.util.ResourceUtil
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    private var mService: ChargingService? = null
    private var mBound = false
    private var serviceIntent: Intent? = null
    private lateinit var pcr: PowerConnectionReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initListener()
        setBroadcastReceiver()
        serviceIntent = Intent(this, ChargingService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent as Intent)
    }

    private fun initListener() {
        batteryPercentageSelector.setOnValueChangedListener { picker: NumberPicker, oldVal: Int, newVal: Int ->
            picker.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY
            )
            if (tvMessage.text.isNotEmpty()) {
                tvMessage.text = String.format(
                    "%s %d%s",
                    getString(R.string.txt_alarm_will_ring),
                    newVal,
                    ResourceUtil.getString(R.string.txt_percentage)
                )
            }
            mService?.setBatteryPercentageForAlarm(newVal)
        }
    }

    private fun setBroadcastReceiver() {
        pcr = PowerConnectionReceiver()
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        this.registerReceiver(pcr, iFilter)
    }

    override fun onStart() {
        super.onStart()

        //bind service with this activity
        serviceIntent?.let { bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE) }
    }

    /**
     * connection for taking callbacks from service class
     */
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: ChargingService.LocalBinder = service as ChargingService.LocalBinder
            mService = binder.getService()
            mBound = true
            initObserver()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    private fun initObserver() {
        mService?.getChargingLiveData()?.observe(this, Observer { chargingResponse ->
            setUpUi(chargingResponse.state, chargingResponse.percentage)
        })
    }

    private fun setUpUi(status: Int, batteryPct: Float) {
        showBattery.percent = batteryPct
        tvBattery.text =
            String.format(
                "%d%s",
                batteryPct.toInt(),
                ResourceUtil.getString(R.string.txt_percentage)
            )
        if (status == 2) {
            ivState.isSelected = true
            tvStatus.isSelected = true
            tvStatus.text = String.format(
                "%s",
                ResourceUtil.getString(R.string.txt_charging)
            )
            tvMessage.text = String.format(
                "%s %d%s",
                getString(R.string.txt_alarm_will_ring),
                DataManager.getBatteryPercentage(),
                ResourceUtil.getString(R.string.txt_percentage)
            )
        } else if (status == 3) {
            ivState.isSelected = false
            tvStatus.isSelected = false
            tvStatus.text = String.format(
                "%s",
                ResourceUtil.getString(R.string.txt_not_charging)
            )
            tvMessage.text = null
        }
        batteryPercentageSelector.value = DataManager.getBatteryPercentage()
    }

    override fun onStop() {
        super.onStop()

        //unbind service
        if (mBound) {
            unbindService(connection)
            mBound = false
        }
    }

    fun click(view: View) {}
}
