package dev.israpil.applockerworkshop.utils

import android.app.AppOpsManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.WindowManager

val Context.appOpsService
    get() = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

val Context.windowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

val Context.canDrawOverlays
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(this)
    } else {
        true
    }

val Context.packageUri: Uri?
    get() = Uri.parse("package:${packageName}")
