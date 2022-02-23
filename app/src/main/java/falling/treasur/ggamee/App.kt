package falling.treasur.ggamee

import android.app.Application
import com.onesignal.OneSignal
import falling.treasur.ggamee.util.Constants.Companion.ONESIGNAL_KEY

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_KEY)
    }
}