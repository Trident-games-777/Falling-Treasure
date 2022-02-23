package falling.treasur.ggamee.util

import android.content.Context
import androidx.core.net.toUri
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import falling.treasur.ggamee.util.Constants.Companion.LINK
import java.util.*

class LinkParser(private val context: Context) {
    fun parseLink(deepLink: String, data: MutableMap<String, Any>?): String {
        val gadid = AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()

        return LINK.toUri().buildUpon().apply {
            appendQueryParameter(Constants.SECURE_GET_PARAMETR, Constants.SECURE_KEY)
            appendQueryParameter(Constants.DEV_TMZ_KEY, TimeZone.getDefault().id)
            appendQueryParameter(Constants.GADID_KEY, gadid)
            appendQueryParameter(Constants.DEEPLINK_KEY, deepLink)
            appendQueryParameter(Constants.SOURCE_KEY, data?.get("media_source").toString())
            appendQueryParameter(
                Constants.AF_ID_KEY,
                AppsFlyerLib.getInstance().getAppsFlyerUID(context)
            )
            appendQueryParameter(Constants.ADSET_ID_KEY, data?.get("adset_id").toString())
            appendQueryParameter(Constants.CAMPAIGN_ID_KEY, data?.get("campaign_id").toString())
            appendQueryParameter(Constants.APP_CAMPAIGN_KEY, data?.get("campaign").toString())
            appendQueryParameter(Constants.ADSET_KEY, data?.get("adset").toString())
            appendQueryParameter(Constants.ADGROUP_KEY, data?.get("adgroup").toString())
            appendQueryParameter(Constants.ORIG_COST_KEY, data?.get("orig_cost").toString())
            appendQueryParameter(Constants.AF_SITEID_KEY, data?.get("af_siteid").toString())
        }.toString()
    }
}