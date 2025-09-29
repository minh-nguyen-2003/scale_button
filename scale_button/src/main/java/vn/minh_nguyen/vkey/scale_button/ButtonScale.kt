package vn.minh_nguyen.vkey.scale_button

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import com.google.android.material.button.MaterialButton

class ButtonScale @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    var pressScale: Float = 0.9f
        set(value) {
            field = value.coerceIn(0.1f, 1.0f)
        }

    var pressAnimDuration: Long = 100L

    var pressPivot: PressPivot = PressPivot.CENTER

    init {
        backgroundTintList = null

        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.ButtonView)
            pressScale =
                a.getFloat(R.styleable.ButtonView_pressScale, pressScale)
            pressAnimDuration =
                a.getInt(
                    R.styleable.ButtonView_pressAnimDuration,
                    pressAnimDuration.toInt()
                ).toLong()
            val pivotValue = a.getInt(R.styleable.ButtonView_pressPivot, 0)
            pressPivot = PressPivot.values()[pivotValue]
            a.recycle()
        }

        isClickable = true
        isFocusable = true
    }

    private fun updatePivot() {
        when (pressPivot) {
            PressPivot.CENTER -> {
                pivotX = width / 2f
                pivotY = height / 2f
            }

            PressPivot.TOP -> {
                pivotX = width / 2f
                pivotY = 0f
            }

            PressPivot.BOTTOM -> {
                pivotX = width / 2f
                pivotY = height.toFloat()
            }

            PressPivot.LEFT -> {
                pivotX = 0f
                pivotY = height / 2f
            }

            PressPivot.RIGHT -> {
                pivotX = width.toFloat()
                pivotY = height / 2f
            }

            PressPivot.TOP_LEFT -> {
                pivotX = 0f
                pivotY = 0f
            }

            PressPivot.TOP_RIGHT -> {
                pivotX = width.toFloat()
                pivotY = 0f
            }

            PressPivot.BOTTOM_LEFT -> {
                pivotX = 0f
                pivotY = height.toFloat()
            }

            PressPivot.BOTTOM_RIGHT -> {
                pivotX = width.toFloat()
                pivotY = height.toFloat()
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                updatePivot()
                animate().cancel()
                animate()
                    .scaleX(pressScale)
                    .scaleY(pressScale)
                    .setDuration(pressAnimDuration)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                animate().cancel()
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(pressAnimDuration)
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            }
        }
        return super.onTouchEvent(event)
    }
}