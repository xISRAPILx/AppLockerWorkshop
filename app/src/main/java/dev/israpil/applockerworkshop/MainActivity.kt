package dev.israpil.applockerworkshop

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.israpil.applockerworkshop.service.AppLockerService
import dev.israpil.applockerworkshop.utils.canDrawOverlays
import dev.israpil.applockerworkshop.utils.hasGetUsageStatsPermissions
import dev.israpil.applockerworkshop.utils.requestGetUsageStatsPermission
import dev.israpil.applockerworkshop.utils.requestOverlaySettings
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            if (!canDrawOverlays) {
                requestOverlaySettings()
            }

            if (!hasGetUsageStatsPermissions()) {
                requestGetUsageStatsPermission()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(
                    Intent(this@MainActivity, AppLockerService::class.java)
                )
            } else {
                startService(
                    Intent(this@MainActivity, AppLockerService::class.java)
                )
            }
        }
    }
}
