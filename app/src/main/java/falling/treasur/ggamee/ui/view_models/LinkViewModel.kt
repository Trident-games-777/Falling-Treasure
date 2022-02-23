package falling.treasur.ggamee.ui.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import falling.treasur.ggamee.database.LinkDb
import falling.treasur.ggamee.database.LinkEntity
import falling.treasur.ggamee.util.Constants.Companion.APPSFLYER_KEY
import falling.treasur.ggamee.util.LinkParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LinkViewModel(
    app: Application,
    private val db: LinkDb
) : AndroidViewModel(app) {

    private val linkParser = LinkParser(app.applicationContext)
    private var appsFlyerDataFetched = false
    private var facebookDataFetched = false

    private var _deepLink: String? = null
    private var _dataMap: MutableMap<String, Any>? = null

    private val _parsedLink: MutableLiveData<String> = MutableLiveData()

    val parsedLink: LiveData<String> = _parsedLink
    val deepLink get() = _deepLink
    val dataMap get() = _dataMap

    init {
        AppLinkData.fetchDeferredAppLinkData(app.applicationContext) {
            _deepLink = it?.targetUri.toString()
            facebookDataFetched = true
            if (appsFlyerDataFetched) _parsedLink.postValue(
                linkParser.parseLink(
                    _deepLink!!,
                    _dataMap
                )
            )
        }

        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataFail(p0: String?) {}
            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
            override fun onAttributionFailure(p0: String?) {}
            override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                _dataMap = p0
                appsFlyerDataFetched = true
                if (facebookDataFetched) _parsedLink.postValue(
                    linkParser.parseLink(
                        _deepLink!!,
                        _dataMap
                    )
                )
            }
        }
        AppsFlyerLib.getInstance().init(
            APPSFLYER_KEY,
            conversionDataListener,
            app.applicationContext
        )
        AppsFlyerLib.getInstance().start(app.applicationContext)
    }

    fun saveLinkToDb(linkEntity: LinkEntity) = viewModelScope.launch(Dispatchers.IO) {
        db.linkDao().insertLink(linkEntity)
    }

    fun getLink() = db.linkDao().getLink()
}