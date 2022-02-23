package falling.treasur.ggamee.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import falling.treasur.ggamee.R
import falling.treasur.ggamee.database.LinkDb
import falling.treasur.ggamee.database.LinkEntity
import falling.treasur.ggamee.ui.view_models.LinkViewModel
import falling.treasur.ggamee.ui.view_models.LinkViewModelFactory

class WebViewActivity : AppCompatActivity() {
    lateinit var webView: WebView
    var messageAb: ValueCallback<Array<Uri?>>? = null
    var callback: ValueCallback<Uri>? = null
    private val resultCode = 1


    private val imageTitle = "Image Chooser"
    private val image1 = "image/*"

    private lateinit var viewModel: LinkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val viewModelFactory = LinkViewModelFactory(application, LinkDb(applicationContext))
        viewModel =
            ViewModelProvider(this, viewModelFactory)[LinkViewModel::class.java]


        webView = findViewById(R.id.webView)
        webView.loadUrl(intent.getStringExtra("link")!!)
        webView.webViewClient = LocalClient()
        webView.settings.javaScriptEnabled = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = false

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            //For Android API >= 21 (5.0 OS)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAb = filePathCallback
                selectImageIfNeed()
                return true
            }

            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(applicationContext)
                newWebView.settings.javaScriptEnabled = true
                newWebView.webChromeClient = this
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    private fun selectImageIfNeed() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = image1
        startActivityForResult(
            Intent.createChooser(i, imageTitle),
            resultCode
        )
    }

    private inner class LocalClient : WebViewClient() {

        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if (errorCode == -2) {
                Toast.makeText(this@WebViewActivity, "Error", Toast.LENGTH_LONG).show()
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.getLink().observe(this@WebViewActivity) {
                it?.let { linkEntity ->
                    if (!linkEntity.flag) {
                        if (!url?.contains("trident")!! && !url.contains("fallreasurgamee.monster")) {
                            viewModel.saveLinkToDb(LinkEntity(link = url, flag = true))
                        }
                    }
                }
            }
        }
    }
}