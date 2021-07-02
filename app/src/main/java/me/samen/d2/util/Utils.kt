package me.samen.d2.util

import android.content.Context
import android.text.method.ScrollingMovementMethod
import io.noties.markwon.Markwon
import io.noties.markwon.movement.MovementMethodPlugin
import java.text.SimpleDateFormat
import java.util.*

fun ts() = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

private lateinit var _markwon: Markwon

fun markwon(context: Context): Markwon {
    if (!::_markwon.isInitialized)
        _markwon = Markwon.builder(context)
            .usePlugin(MovementMethodPlugin.create(ScrollingMovementMethod.getInstance())).build()
    return _markwon
}