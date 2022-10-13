package com.example.pictureoftheday.view.pod

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.pictureoftheday.R
import com.example.pictureoftheday.utils.EquilateralImageView

class PODBehavior(context: Context, attrs: AttributeSet?=null) :
    CoordinatorLayout.Behavior<EquilateralImageView>(context,attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: EquilateralImageView,
        dependency: View
    ): Boolean {
        return (dependency.id == R.id.bottom_sheet_layout)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: EquilateralImageView,
        dependency: View
    ): Boolean {
        if (dependency.id == R.id.bottom_sheet_layout){
            child.y = dependency.y - child.height
            if (child.y < 0){
                child.alpha = (child.height + child.y) / child.height
            }
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}