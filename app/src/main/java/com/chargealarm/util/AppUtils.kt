package com.chargealarm.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.chargealarm.App
import com.chargealarm.R
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*

object AppUtils {

    fun hideKeyboard(context: Activity) {
        val inputManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            context.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun showKeyboard(context: Activity) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(context.currentFocus, InputMethodManager.SHOW_IMPLICIT)
    }

    @SuppressLint("HardwareIds")
    fun getUniqueDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * This method is used to access config properties form AppConnectConfig file
     */
    @Throws(IOException::class)
    fun getProperty(key: String, context: Context): String {
        val properties = Properties()
        val assetManager = context.assets
        val inputStream = assetManager.open("appconnectconfig.properties")
        properties.load(inputStream)
        return properties.getProperty(key)
    }

    fun getCompleteAddressString(latitude: Double, longitude: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(App.appContext, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder()

                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("Current loction address", strReturnedAddress.toString())
            } else {
                Log.w("Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("Current loction address", "Cannot get Address!")
        }
        return strAdd
    }

    /**
     * Method to locate view.
     *
     * @param v is used to checking its location on screen to show
     * status popup under it.
     * @return Rect obj to get location of the passed view of the screen.
     */
    fun locateView(v: View): Rect? {
        val locInt = IntArray(2)
        v.getLocationOnScreen(locInt)
        val location = Rect()
        location.left = locInt[0]
        location.top = locInt[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

    fun createBitmapOfMarker(customMarkerView: View): Bitmap {
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        customMarkerView.background?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    /**
     * This method is used to show permission allow from Setting
     */
    fun showAllowPermissionFromSettingDialog(activity: Activity, message: String) {
        AlertDialog.Builder(activity).let {
            it.setMessage(message)
            it.setPositiveButton(activity.getString(R.string.action_settings)) { dialog, id ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", activity.packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)
                dialog.dismiss()
            }
            it.setNegativeButton(activity.getString(R.string.action_cancel)) { dialog, id ->
                dialog.dismiss()
            }
            it.create().show()
        }
    }

    fun showSnackBar(mSnackView: View, message: String): Snackbar {
        val snackBar = Snackbar.make(mSnackView, message, Snackbar.LENGTH_SHORT)
        snackBar.view.let {
            it.setBackgroundColor(ContextCompat.getColor(App.appContext, R.color.black))
            it.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.WHITE)
        }
        snackBar.show()
        return snackBar
    }

    fun showErrorSnackBar(mSnackView: View, message: String): Snackbar {
        val snackBar = Snackbar.make(mSnackView, message, Snackbar.LENGTH_SHORT)
        snackBar.view.let {
            it.setBackgroundColor(ContextCompat.getColor(App.appContext, R.color.red))
            it.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.WHITE)
        }
        snackBar.show()
        return snackBar
    }

    fun vibratePhone() {
        val vibrator = App.appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

}