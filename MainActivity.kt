package com.sabritn.lockclock

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Bouton : ouvrir le sélecteur système de fond d'écran animé,
        // pré-rempli avec ce Live Wallpaper.
        findViewById<Button>(R.id.btnSetWallpaper).setOnClickListener {
            try {
                val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                intent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(this, ClockWallpaperService::class.java)
                )
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Impossible d'ouvrir le sélecteur de fond d'écran", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. Bouton : ouvrir directement les réglages de l'appli pour que
        // l'utilisateur active manuellement "Afficher sur écran verrouillé"
        // (réglage propre à MIUI, non accessible par code).
        findViewById<Button>(R.id.btnLockPermission).setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
            Toast.makeText(
                this,
                "Dans Autorisations, active « Afficher sur écran verrouillé » et « Démarrage automatique »",
                Toast.LENGTH_LONG
            ).show()
        }

        // 3. Bouton : demander l'exemption d'optimisation de batterie,
        // indispensable sur MIUI pour que le service ne soit pas tué.
        findViewById<Button>(R.id.btnBattery).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val pm = getSystemService(POWER_SERVICE) as PowerManager
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Déjà activé", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
