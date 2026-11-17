package kz.vtgk.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Приложение «Колледж ВТГК».
 * Весь интерфейс — app/src/main/assets/index.html (WebView).
 * Чтобы обновить дизайн, замените index.html и пересоберите APK.
 */
class MainActivity : ComponentActivity() {

    private lateinit var root: FrameLayout
    private lateinit var web: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        root = FrameLayout(this).apply { setBackgroundColor(Color.parseColor("#0E1013")) }
        web = WebView(this).apply {
            setBackgroundColor(Color.parseColor("#0E1013"))
            overScrollMode = WebView.OVER_SCROLL_NEVER
        }
        root.addView(web, FrameLayout.LayoutParams(-1, -1))
        setContentView(root)

        // Отступы под системные панели (статус-бар и навигация)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            WindowInsetsCompat.CONSUMED
        }

        with(web.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true      // сохранение темы и чек-листа
            allowFileAccess = true
            mediaPlaybackRequiresUserGesture = false
            textZoom = 100
        }
        web.addJavascriptInterface(Bridge(), "VTGK")

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url
                if (url.scheme == "file") return false
                // tel:, mailto:, соцсети и сайты открываем во внешних приложениях
                return try { startActivity(Intent(Intent.ACTION_VIEW, url)); true } catch (e: Exception) { true }
            }
        }

        web.loadUrl("file:///android_asset/index.html")

        // Кнопка «Назад»: закрывает шторки / возвращает на главную, затем выходит
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                web.evaluateJavascript("(window.vtgkBack && window.vtgkBack()) ? '1' : '0'") { r ->
                    if (r != "\"1\"") finish()
                }
            }
        })
    }

    /** Вызывается из index.html при смене цветового режима */
    inner class Bridge {
        @JavascriptInterface
        fun setBars(hex: String, light: Boolean) {
            runOnUiThread {
                try { root.setBackgroundColor(Color.parseColor(hex)) } catch (_: Exception) {}
                WindowCompat.getInsetsController(window, root).apply {
                    isAppearanceLightStatusBars = light
                    isAppearanceLightNavigationBars = light
                }
            }
        }
    }

    override fun onDestroy() { web.destroy(); super.onDestroy() }
}
