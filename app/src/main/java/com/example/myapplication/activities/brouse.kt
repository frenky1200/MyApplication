package com.example.myapplication.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.DownloadListener
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import android.graphics.Bitmap
import android.webkit.URLUtil

import com.example.myapplication.R

import java.io.File

class brouse : Activity() {
    private var mFullscreenViewCallback: WebChromeClient.CustomViewCallback? = null
    private var mFullScreenContainer: FrameLayout? = null
    private var mFullScreenView: View? = null
    private var mWebView: WebView? = null
    internal var urload: String? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mCapturedImageURI: Uri? = null
    private var downloadManager: DownloadManager? = null

    // тянем видео на весь экран
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

        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {

            // Сообщение об обновлении
            mUploadMessage = uploadMsg

            try {
                val imageStorageDir = File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES), "AndroidExampleFolder")

                if (!imageStorageDir.exists()) {
                    // Создать AndroidExampleFolder в sdcard
                    imageStorageDir.mkdirs()
                }

                // Создать камеру захваченное изображение путь к файлу и имя
                val file = File(
                        imageStorageDir.toString() + File.separator + "IMG_"
                                + System.currentTimeMillis().toString()
                                + ".jpg")

                mCapturedImageURI = Uri.fromFile(file)

                // Камера захвата изображения  intent
                val captureIntent = Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE)

                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)

                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"

                // Создать файл селектора intent
                val chooserIntent = Intent.createChooser(i, "Image Chooser")

                // Установить камеры намерении выбора файлов
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent))

                // На выбор изображения обхода метода onactivityresult вызов метода активности
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)

            } catch (e: Exception) {
                Toast.makeText(baseContext, "Exception:$e",
                        Toast.LENGTH_LONG).show()
            }

        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
            openFileChooser(uploadMsg, "")
        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>,
                            acceptType: String,
                            capture: String) {
            openFileChooser(uploadMsg, acceptType)
        }

        override fun onConsoleMessage(cm: ConsoleMessage): Boolean {

            onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId())
            return true
        }

        override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
            //Log.d("androidruntime", "Show console messages, Used for debugging: " + message);

        }
    }// End setWebChromeClient

    private val data: Any? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.brouse)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                    loadEnd()
                }
            }
        }
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        mWebView = findViewById(R.id.web_view)
        mFullScreenContainer = findViewById(R.id.fullscreen_container)
        mWebView!!.webChromeClient = mWebChromeClient
        mWebView!!.loadUrl("http://yandex.ru")
        handleIntent(intent)

        class HelloWebViewClient : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                findViewById<View>(R.id.progress1).visibility = View.VISIBLE
                title = url
                urload = mWebView!!.url
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                // запускаем ссылки на маркет
                val uri = Uri.parse(url)
                if (uri.scheme == "market") {
                    val i = Intent(android.content.Intent.ACTION_VIEW)
                    i.data = uri
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    mWebView!!.canGoBack()
                    run { mWebView!!.goBack() }
                }
                // запускаем email
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
                // запускаем звонилку
                if (uri.scheme == "tel") {
                    val i = Intent(android.content.Intent.ACTION_DIAL)
                    i.data = uri
                    startActivity(i)

                    mWebView!!.canGoBack()
                    run { mWebView!!.goBack() }
                }
                // запускаем лoкцию
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

        mWebView!!.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimetype)

            val downloadDialog = AlertDialog.Builder(this@brouse)
            downloadDialog.setTitle("Менеджер загрузок")
            downloadDialog.setMessage("Загрузить этот файл в папку Donwload ?" + 'n'.toString() + mimetype + 'n'.toString() + url)
            downloadDialog.setPositiveButton("Да") { dialogInterface, i ->
                doDownload(url, fileName)
                dialogInterface.dismiss()
            }
            downloadDialog.setNegativeButton("Нет") { dialogInterface, i -> }
            downloadDialog.show()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public override// настройки
    fun onResume() {
        super.onResume()
        mWebView!!.settings.loadsImagesAutomatically = true
        mWebView!!.settings.javaScriptEnabled = true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {// кнопка назад
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            mWebView!!.canGoBack()
            run { mWebView!!.goBack() }
            return true
        }
        return super.onKeyDown(keyCode, event)
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

    private fun doDownload(url: String, fileName: String) {
        val uriOriginal = Uri.parse(url)
        try {
            downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url))
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            request.setTitle(fileName)
            request.setDescription("My description")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            downloadManager!!.enqueue(request)
            Toast.makeText(this@brouse, "Downloading $fileName", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
            Log.e("", "Problem downloading: $uriOriginal", e)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        val urlPage2 = data.getStringExtra("urlPage2")
        mWebView!!.loadUrl(urlPage2)
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == this.mUploadMessage) {
                return
            }
            var result: Uri? = null
            try {
                if (resultCode != Activity.RESULT_OK) {
                    result = null
                } else {
                    // извлечь из собственной переменной, если намерение состоит в нуль
                    result = if (data == null) mCapturedImageURI else data.data
                }
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "activity :$e",
                        Toast.LENGTH_LONG).show()
            }

            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        }
    }

    fun loadEnd() {
        Toast.makeText(this, "Файл Загружен в папку Donwload", Toast.LENGTH_SHORT).show()
    }

    public override fun onDestroy() {
        super.onDestroy()
        mWebView!!.stopLoading()
        mWebView!!.clearCache(true)
        mWebView!!.clearView()
        mWebView!!.freeMemory()
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

    companion object {

        private val FILECHOOSER_RESULTCODE = 2888
    }
}