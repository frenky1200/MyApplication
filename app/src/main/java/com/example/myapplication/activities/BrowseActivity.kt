package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import com.example.myapplication.R

class BrowseActivity : Activity() {
    private var mFullscreenViewCallback: WebChromeClient.CustomViewCallback? = null
    private var mFullScreenContainer: FrameLayout? = null
    private var mFullScreenView: View? = null
    private var mWebView: WebView? = null
    private lateinit var urload: String
    private var downloadManager: DownloadManager? = null

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onShowCustomView(view: View, requestedOrientation: Int, callback: WebChromeClient.CustomViewCallback) {
            onShowCustomView(view, callback)
        }

        override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
            if (mFullScreenView != null) {
                callback.onCustomViewHidden()
                return
            }
            mFullScreenView = view
            mWebView!!.visibility = View.GONE
            mFullScreenContainer!!.visibility = View.VISIBLE
            mFullScreenContainer!!.addView(view)
            mFullscreenViewCallback = callback
        }

        override fun onHideCustomView() {
            super.onHideCustomView()
            if (mFullScreenView == null) {
                return
            }
            mWebView!!.visibility = View.VISIBLE
            mFullScreenView!!.visibility = View.GONE
            mFullScreenContainer!!.visibility = View.GONE
            mFullScreenContainer!!.removeView(mFullScreenView)
            mFullscreenViewCallback!!.onCustomViewHidden()
            mFullScreenView = null
        }


    }

    public override fun onCreate(savedInstanceState: Bundle?) {

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (prefs.getString("colors", "")) {

            "Серый" -> setTheme(R.style.AppTheme)
            "Красный" -> setTheme(R.style.Red)
            "Зеленый" -> setTheme(R.style.Green)
            "Синий" -> setTheme(R.style.Blue)
            "Желтый" -> setTheme(R.style.Yellow)
            "Пурпурный" -> setTheme(R.style.Purple)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.brouse)

        mWebView = findViewById(R.id.web_view)
        mFullScreenContainer = findViewById(R.id.fullscreen_container)
        mWebView!!.webChromeClient = mWebChromeClient
        val search = intent.extras?.getString("search", "")?:""
        mWebView!!.loadUrl("https://yandex.by/$search")
        handleIntent(intent)

        class HelloWebViewClient : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                findViewById<View>(R.id.progress1).visibility = View.VISIBLE
                title = url
                urload = mWebView!!.url
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                val uri = Uri.parse(url)
                if (uri.scheme == "market") {
                    val i = Intent(android.content.Intent.ACTION_VIEW)
                    i.data = uri
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    mWebView!!.canGoBack()
                    run { mWebView!!.goBack() }
                }
                if (uri.scheme == "mailto") {
                    val i = Intent(android.content.Intent.ACTION_SEND)
                    i.type = "text/html"
                    i.putExtra(Intent.EXTRA_SUBJECT, "Введите тему")
                    i.putExtra(Intent.EXTRA_TEXT, "Введите текст")
                    i.putExtra(Intent.EXTRA_EMAIL, arrayOf(url))
                    startActivity(i)
                    mWebView!!.canGoBack()
                    run { mWebView!!.goBack() }

                }
                if (uri.scheme == "tel") {
                    val i = Intent(android.content.Intent.ACTION_DIAL)
                    i.data = uri
                    startActivity(i)

                    mWebView!!.canGoBack()
                    run { mWebView!!.goBack() }
                }
                if (uri.scheme == "geo") {
                    val i = Intent(android.content.Intent.ACTION_VIEW)
                    i.data = uri
                    startActivity(i)

                    mWebView!!.canGoBack()
                    run { mWebView!!.goBack() }
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                findViewById<View>(R.id.progress1).visibility = View.GONE
            }

            override fun onReceivedError(view: WebView, errorCode: Int,
                                         description: String, failingUrl: String) {
                mWebView!!.loadUrl("file:///android_asset/error.png")
            }
        }

        mWebView!!.webViewClient = HelloWebViewClient()

        mWebView!!.setDownloadListener { url, _,
                                         contentDisposition, mimetype, _ ->
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimetype)

            val downloadDialog = AlertDialog.Builder(this@BrowseActivity)
            downloadDialog.setTitle("Менеджер загрузок")
            downloadDialog.setMessage("Загрузить этот файл в папку Download ?\n$mimetype\n$url")
            downloadDialog.setPositiveButton("Да") { dialogInterface, _ ->
                doDownload(url, fileName, mimetype)
                dialogInterface.dismiss()
            }
            downloadDialog.setNegativeButton("Нет") { _, _ -> }
            downloadDialog.show()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    public override fun onResume() {
        super.onResume()
        mWebView!!.settings.loadsImagesAutomatically = true
        mWebView!!.settings.javaScriptEnabled = true
    }

    private fun handleIntent(intent: Intent): Boolean {
        val action = intent.action
        if (Intent.ACTION_VIEW == action) {
            val url = intent.dataString
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
            mWebView!!.loadUrl(url)// грузим страницу
            return true
        }
        return false
    }

    private fun doDownload(url: String, fileName: String, mime:String) {
        val uriOriginal = Uri.parse(url)
        try {
            downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            val request = DownloadManager.Request(Uri.parse(url))
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            request.setTitle(fileName)
            request.allowScanningByMediaScanner()
            request.setDescription("My description")
            request.setMimeType(mime)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.allowScanningByMediaScanner()
            downloadManager!!.enqueue(request)}

            Toast.makeText(this@BrowseActivity, "Downloading $fileName", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
            Log.e("", "Problem downloading: $uriOriginal", e)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        mWebView!!.stopLoading()
        mWebView!!.clearCache(true)
        mWebView!!.destroy()
        mWebView = null
    }

    override fun onBackPressed() {
        if (mWebView!!.canGoBack()) {
            mWebView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}