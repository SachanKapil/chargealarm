package com.chargealarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.chargealarm.data.model.ChargingResponse
import com.chargealarm.service.ChargingService

class PowerConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level.times(100).div(scale.toFloat())
        if (context is ChargingService) {
            context.sendBatteryStatusReport(
                ChargingResponse(
                    status,
                    batteryPct
                )
            )
        }
    }
}