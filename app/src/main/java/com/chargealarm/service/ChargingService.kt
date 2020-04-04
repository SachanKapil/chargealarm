package com.chargealarm.service

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.chargealarm.App
import com.chargealarm.R
import com.chargealarm.data.DataManager
import com.chargealarm.data.model.ChargingResponse
import com.chargealarm.receiver.PowerConnectionReceiver
import com.chargealarm.ui.home.HomeActivity
import com.chargealarm.util.ResourceUtil

class ChargingService : Service() {

    private val binder: IBinder = LocalBinder()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var pcr: PowerConnectionReceiver
    private val channelId: String = "1"
    private val notificationId = 1
    private val chargingLiveData = MutableLiveData<ChargingResponse>()

    /**
     * LocalBinder class return context of this class to call its public methods
     */
    inner class LocalBinder : Binder() {
        fun getService(): ChargingService {
            return this@ChargingService
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.audio)
        if (DataManager.getBatteryPercentage() == 0) {
            DataManager.saveBatteryPercentage(90)
        }
        setBroadcastReceiver()

        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
        builder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.BigTextStyle())
            .setSmallIcon(getNotificationIcon())
            .setContentIntent(getNotificationPendingIntent())
            .setOngoing(true)

        //start foreground notification to show the current status of song to user
        val notification: Notification = builder.build()
        startForeground(notificationId, notification)
    }

    private fun setBroadcastReceiver() {
        pcr = PowerConnectionReceiver()
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        this.registerReceiver(pcr, iFilter)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    @Nullable
    @Override
    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel =
                NotificationChannel(channelId, getString(R.string.app_name), importance)
            channel.description = getString(R.string.app_name)
            channel.setShowBadge(true)
            channel.enableVibration(false)
            channel.setSound(null, null)
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channel.lightColor = ContextCompat.getColor(this, R.color.colorPrimary)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun playSong() {
        mediaPlayer.start()
        mediaPlayer.isLooping = true
    }

    private fun stopSong() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer.create(App.appContext, R.raw.audio)
        }
    }

    fun sendBatteryStatusReport(response: ChargingResponse) {

        builder.setContentText(
            String.format(
                "%s %d%s",
                getString(R.string.txt_battery_percentage),
                response.percentage.toInt(),
                ResourceUtil.getString(R.string.txt_percentage)
            )
        )

        if (response.state == 2) {
            builder.setContentTitle(
                String.format(
                    "%s %d%s",
                    getString(R.string.txt_alarm_will_ring),
                    DataManager.getBatteryPercentage(),
                    ResourceUtil.getString(R.string.txt_percentage)
                )
            )
            if (response.percentage.toInt() >= DataManager.getBatteryPercentage()) {
                playSong()
            } else {
                stopSong()
            }
        }
        if (response.state == 3) {
            builder.setContentTitle(null)
            stopSong()
        }
        val notification = builder.build()
        notificationManager.notify(notificationId, notification)
        chargingLiveData.value = response
    }

    fun getChargingLiveData(): MutableLiveData<ChargingResponse> {
        return chargingLiveData
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP
        //small icon we have to take from design team for greater os versions
        return if (useWhiteIcon) R.mipmap.ic_launcher else R.mipmap.ic_launcher
    }

    private fun getNotificationPendingIntent(): PendingIntent? {
        val clickIntent = Intent(this, HomeActivity::class.java)
        return PendingIntent.getActivity(
            this, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        //reset and release media player object
        mediaPlayer.reset()
        mediaPlayer.release()
        unregisterReceiver(pcr)
        stopForeground(true)
        stopService(Intent(this, ChargingService::class.java))
        super.onTaskRemoved(rootIntent)
    }

    fun setBatteryPercentageForAlarm(percentage: Int) {
        if (mediaPlayer.isPlaying && percentage > DataManager.getBatteryPercentage()) {
            stopSong()
        }
        DataManager.saveBatteryPercentage(percentage)
    }
}