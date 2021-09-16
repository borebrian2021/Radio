package com.example.radiochuka

import android.app.DownloadManager
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fuela.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar


@Suppress("DEPRECATION")
class MainActivity :  AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    internal var url = "http://chukaradio-001-site1.itempurl.com/"
    private val PERMISSION_REQUEST_CODE = 1

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        when (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_LARGE -> this.window.setLayout(900, 755)
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> this.window.setLayout(1080, 1000) //width x height
        }
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        showToast()

    }
    override fun onBackPressed() {
       showMessageBox()
    }
    fun showMessageBox(){

        val build = AlertDialog.Builder(this)
        build.setTitle("Confirm")
        build.setMessage("Do you want to run radio in background?")
        build.setPositiveButton("YES") { dialog, which ->

        }
        build.setNegativeButton("Yes") { dialog, which ->

            super.onBackPressed();
        }
        build.setNeutralButton("No") { _, _ ->
            finish();

        }
        val dialog: AlertDialog = build.create()
        dialog.show()
    }

     @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
     fun showToast() {
        val ConnectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            Toast.makeText(this@MainActivity, "Network Available", Toast.LENGTH_LONG).show()


//            Toast.makeText(this, "You are offline now.!!!", Toast.LENGTH_LONG).show()
            val web_holder = findViewById<LinearLayout>(R.id.web_view_holder);
            val no_net = findViewById<LinearLayout>(R.id.no_internet);
            val webview = findViewById<WebView>(R.id.webView);

            web_holder.visibility=View.VISIBLE
            no_net.visibility=View.GONE
                webview.settings.setAppCacheEnabled(true)
                webview.settings.cacheMode = WebSettings.LOAD_DEFAULT
                webview.settings.setAppCachePath(cacheDir.path)
                webview.settings.javaScriptEnabled = true
                webview.settings.builtInZoomControls = false
                webview.settings.displayZoomControls = false
                // More web view settings
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    webview.settings.safeBrowsingEnabled = true  // api 26
                }

                //settings.pluginState = WebSettings.PluginState.ON
                webview.settings.useWideViewPort = true
                webview.settings.loadWithOverviewMode = true
                webview.settings.javaScriptCanOpenWindowsAutomatically = true
                webview.settings.mediaPlaybackRequiresUserGesture = false
            webview.settings.domStorageEnabled = true;
            webview.webChromeClient = WebChromeClient()
            webview.webViewClient = WebViewClient()
                webview.loadUrl(url)

                webview.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.setMimeType(mimeType)
                    request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
                    request.addRequestHeader("User-Agent", userAgent)
                    request.setDescription("Downloading file...")
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
                    request.allowScanningByMediaScanner()
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalFilesDir(this@MainActivity, Environment.DIRECTORY_DOWNLOADS, ".pdf")
                    val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(request)
                    Toast.makeText(applicationContext, "Downloading File", Toast.LENGTH_LONG).show()
                }


        } else {
            //INTERNET IS AVAILLABLE
            Toast.makeText(this@MainActivity, "Network Not Available", Toast.LENGTH_LONG).show()

            val web_holder = findViewById<LinearLayout>(R.id.web_view_holder);
            val no_net = findViewById<LinearLayout>(R.id.no_internet);
            val webview = findViewById<WebView>(R.id.webView);
            web_holder.visibility=View.GONE
            no_net.visibility=View.VISIBLE


//            if (networkType()) {
//                Toast.makeText(this, "You are online now.!!!" + "\n Connected to Wifi Network", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "You are online now.!!!" + "\n Connected to Cellular Network", Toast.LENGTH_LONG).show()
//            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        TODO("Not yet implemented")
    }
}