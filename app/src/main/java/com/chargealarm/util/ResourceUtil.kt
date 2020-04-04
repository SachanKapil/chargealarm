package com.chargealarm.util

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.chargealarm.App
import com.chargealarm.R

object ResourceUtil {

    fun getResourceIdFromResourceName(drawableName: String): Int {
        try {
            val res = R.drawable::class.java
            val field = res.getField(drawableName)
            return field.getInt(null)
        } catch (e: Exception) {

        }
        return -1
    }

    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(App.appContext, resId)
    }

    fun getColor(colorResId: Int): Int {
        return ContextCompat.getColor(App.appContext, colorResId)
    }

    fun getString(resId: Int): String {
        return App.appContext.getString(resId)
    }

    fun getStringArray(resId: Int): ArrayList<String> {
        val stringArray: Array<String> = App.appContext.resources.getStringArray(resId)
        return stringArray.toCollection(ArrayList())
    }

    fun getFont(font: Int): Typeface? {
        return ResourcesCompat.getFont(App.appContext, font)
    }
}