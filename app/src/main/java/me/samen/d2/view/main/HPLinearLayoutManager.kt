package me.samen.d2.view.main

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager

/*

SOURCE : https://stackoverflow.com/a/38501009

FOR THE APP

        // TODO(satosh.dhanyamraju): dagger

 */
class HPLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    /**
     * Magic here
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}