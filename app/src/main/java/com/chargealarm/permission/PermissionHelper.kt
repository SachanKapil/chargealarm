package com.chargealarm.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.chargealarm.constant.AppConstants

class PermissionHelper(var mGetPermissionListener: IGetPermissionListener) {

    private lateinit var mActivity: Activity

    fun hasPermission(context: Activity, permissions: Array<String>, requestCode: Int): Boolean {
        mActivity = context
        var isAllGranted = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isAllGranted = false
            }
        }
        return if (!isAllGranted) {
            ActivityCompat.requestPermissions(context, permissions, requestCode)
            false
        } else {
            true
        }
    }

    fun hasPermission(fragment: Fragment, permissions: Array<String>, requestCode: Int): Boolean {
        mActivity = fragment.activity!!
        var isAllGranted = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    fragment.context!!,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isAllGranted = false
            }
        }
        return if (!isAllGranted) {
            fragment.requestPermissions(permissions, requestCode)
            false
        } else true
    }

    fun setPermissionResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            AppConstants.PermissionConstants.REQ_CODE_GALLERY -> {
                //If user denies the READ external storage permission with don't ask again , then this variable will be true
                val isStorage = shouldShowRequestPermission(permissions[0])
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGetPermissionListener.permissionGiven(AppConstants.PermissionConstants.REQ_CODE_GALLERY)
                } else if (isStorage) {
                    mGetPermissionListener.permissionDenied(AppConstants.PermissionConstants.MESSAGE_CODE_STORAGE)
                }
            }

            AppConstants.PermissionConstants.REQ_CODE_CAMERA -> {
                //If user denies the CAMERA permission with don't ask again checkbox,then this variable will be true
                val isCamera = shouldShowRequestPermission(permissions[0])
                //If user denies the WRITE permission with don't ask again checkbox,then this variable will be true
                val isStorage = shouldShowRequestPermission(permissions[1])
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mGetPermissionListener.permissionGiven(AppConstants.PermissionConstants.REQ_CODE_CAMERA)
                } else if (isCamera && isStorage) {
                    mGetPermissionListener.permissionDenied(AppConstants.PermissionConstants.MESSAGE_CODE_CAMERA_STORAGE)
                } else if (isCamera) {
                    mGetPermissionListener.permissionDenied(AppConstants.PermissionConstants.MESSAGE_CODE_CAMERA)
                } else if (isStorage) {
                    mGetPermissionListener.permissionDenied(AppConstants.PermissionConstants.MESSAGE_CODE_STORAGE)
                }
            }
        }
    }

    private fun shouldShowRequestPermission(permission: String): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)
                && ContextCompat.checkSelfPermission(
            mActivity,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }


    /**
     * This interface is used to get the user permission callback to the activity or fragment who
     * implements it
     */
    interface IGetPermissionListener {
        fun permissionGiven(requestCode: Int)
        fun permissionDenied(messageCode: Int)
    }
}