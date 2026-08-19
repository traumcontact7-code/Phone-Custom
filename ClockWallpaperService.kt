package com.sabritn.lockclock

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Live Wallpaper affichant une horloge géante semi-transparente
 * et la date, dans le style d'un thème MIUI (chiffres larges en
 * surimpression, dégradé du haut vers le bas).
 *
 * S'affiche sur l'écran d'accueil. Pour l'écran VERROUILLÉ, MIUI
 * exige en plus que l'utilisateur active manuellement l'option
 * "Afficher sur écran verrouillé" dans les réglages de l'appli
 * (voir MainActivity) — ce n'est pas activable par code seul.
 */
class ClockWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine = ClockEngine()

    inner class ClockEngine : Engine() {

        private val handler = Handler(Looper.getMainLooper())
        private var visible = true

        private val timePaint = Paint().apply {
            isAntiAlias = true
            color = Color.argb(215, 255, 255, 255)
            textAlign = Paint.Align.CENTER
            typeface = android.graphics.Typeface.create("sans-serif-thin", android.graphics.Typeface.NORMAL)
        }

        private val datePaint = Paint().apply {
            isAntiAlias = true
            color = Color.argb(230, 255, 255, 255)
            textAlign = Paint.Align.CENTER
            textSize = 42f
            typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
        }

        private val bgPaint = Paint()

        private val drawRunner = object : Runnable {
            override fun run() {
                drawFrame()
                handler.removeCallbacks(this)
                if (visible) {
                    // Rafraîchit chaque seconde pour faire avancer l'horloge
                    handler.postDelayed(this, 1000L)
                }
            }
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
        }

        override fun onVisibilityChanged(visibleNow: Boolean) {
            visible = visibleNow
            if (visible) {
                handler.post(drawRunner)
            } else {
                handler.removeCallbacks(drawRunner)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            visible = false
            handler.removeCallbacks(drawRunner)
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    drawBackground(canvas)
                    drawClock(canvas)
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private fun drawBackground(canvas: Canvas) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            // Dégradé montagne/coucher de soleil, à remplacer par une vraie
            // photo (voir setBackgroundImage ci-dessous) si tu veux ta
            // propre image comme dans la capture d'écran.
            val gradient = LinearGradient(
                0f, 0f, 0f, h,
                intArrayOf(
                    Color.parseColor("#D9A7B3"),
                    Color.parseColor("#8E7A96"),
                    Color.parseColor("#2E2A3D")
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
            bgPaint.shader = gradient
            canvas.drawRect(0f, 0f, w, h, bgPaint)
        }

        private fun drawClock(canvas: Canvas) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()

            // Taille du texte proportionnelle à la largeur de l'écran,
            // comme les gros chiffres transparents de la capture.
            timePaint.textSize = w * 0.34f

            val cal = Calendar.getInstance()
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("MM-dd EEEE", Locale.getDefault())

            val timeText = timeFormat.format(cal.time)
            val dateText = dateFormat.format(cal.time)

            canvas.drawText(dateText, w / 2f, h * 0.42f, datePaint)
            canvas.drawText(timeText, w / 2f, h * 0.58f, timePaint)
        }
    }
}
