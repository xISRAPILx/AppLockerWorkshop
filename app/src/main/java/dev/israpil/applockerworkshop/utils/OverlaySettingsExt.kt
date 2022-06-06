package dev.israpil.applockerworkshop.utils

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun ComponentActivity.requestOverlaySettings() = suspendCancellableCoroutine<Unit> {
    startOverlaySettings()

    val observer = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            lifecycle.removeObserver(this)

            it.resume(Unit)
        }
    }

    lifecycle.addObserver(observer)
    it.invokeOnCancellation {
        lifecycle.addObserver(observer)
    }
}

private fun Activity.startOverlaySettings() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        startActivity(
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = packageUri
            }
        )
    }
}
