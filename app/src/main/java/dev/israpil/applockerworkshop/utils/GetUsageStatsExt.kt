package dev.israpil.applockerworkshop.utils

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

fun Context.hasGetUsageStatsPermissions(): Boolean =
    appOpsService.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        applicationInfo.uid,
        applicationInfo.packageName,
    ) == AppOpsManager.MODE_ALLOWED

/**
 * Приостанавливает выполнение корутины до получения прав
 */
suspend fun Activity.requestGetUsageStatsPermission() =
    suspendCancellableCoroutine<Unit> {
        startStatsSettings()

        val opListener = object : AppOpsManager.OnOpChangedListener {
            override fun onOpChanged(op: String?, packageName: String?) {
                if (hasGetUsageStatsPermissions()) {
                    appOpsService.stopWatchingMode(this)

                    it.resume(Unit)
                }
            }
        }

        appOpsService.startWatchingMode(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            packageName,
            opListener
        )

        it.invokeOnCancellation {
            appOpsService.stopWatchingMode(opListener)
        }
    }

private fun Context.startStatsSettings() {
    startActivity(
        Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            data = packageUri
        }
    )
}

