package com.chargealarm.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.chargealarm.R

class RoundedCornersImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        val radius = context.resources.getDimension(R.dimen._4sdp)
        val path = Path()
        val rect = RectF(0F, 0F, this.width.toFloat(), this.height.toFloat())
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    fun clearOverlay() {
        clearColorFilter()
    }

    fun putOverlay() {
        setColorFilter(
            ContextCompat.getColor(context, R.color.black_30_alpha), PorterDuff.Mode.SRC_OVER
        )
    }

    fun putDarkOverlay() {
        setColorFilter(
            ContextCompat.getColor(context, R.color.black_60_alpha),
            PorterDuff.Mode.SRC_OVER
        )
    }
}