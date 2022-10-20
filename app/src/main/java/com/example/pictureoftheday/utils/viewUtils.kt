package com.example.pictureoftheday.utils

import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pictureoftheday.view.pod.OnImageViewAnimationEnd

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, 0, 250)
        show()
    }
}

fun View.scaleView(startScale: Float, endScale: Float, pivotX: Float, pivotY: Float, callback: OnImageViewAnimationEnd) {
    val anim: Animation = ScaleAnimation(
        startScale, endScale,
        startScale, endScale,
        Animation.RELATIVE_TO_SELF, pivotX,
        Animation.RELATIVE_TO_SELF, pivotY
    )
    anim.fillAfter = true
    anim.duration = 1000
    anim.setAnimationListener(object : Animation.AnimationListener{
        override fun onAnimationStart(p0: Animation?) {
            // не требуется
        }
        override fun onAnimationEnd(p0: Animation?) {
            callback.onEnd()
        }
        override fun onAnimationRepeat(p0: Animation?) {
            // не требуется
        }
    })
    this.startAnimation(anim)
}
