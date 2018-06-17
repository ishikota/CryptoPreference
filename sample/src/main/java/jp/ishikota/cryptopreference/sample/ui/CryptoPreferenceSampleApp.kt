package jp.ishikota.cryptopreference.sample.ui

import android.app.Application
import com.facebook.stetho.Stetho
import jp.ishikota.cryptopreference.CryptoPreference

class CryptoPreferenceSampleApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // setup for  CryptoPreference
        CryptoPreference.Initializer.apply {
            secretKeyForCompat = "0123456789123456".toByteArray()
            debugMode = true
            preferenceName = CRYPTO_PREFERENCE_NAME
        }

        // setup for Stetho
        Stetho.initializeWithDefaults(this)
    }

    companion object {
        const val CRYPTO_PREFERENCE_NAME = "CryptoPreference"
        const val APP_PREFERENCE_NAME = "CryptoPreferenceSample"
    }

}
