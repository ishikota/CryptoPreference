package jp.ishikota.cryptopreference.sample.ui

import android.app.Application
import jp.ishikota.cryptopreference.CryptoPreference

class CryptoPreferenceSampleApp: Application() {

    override fun onCreate() {
        super.onCreate()
        CryptoPreference.Initializer.apply {
            secretKeyForCompat = byteArrayOf()
            preferenceName = CRYPTO_PREFERENCE_NAME
        }
    }

    companion object {
        const val CRYPTO_PREFERENCE_NAME = "CryptoPreference"
        const val APP_PREFERENCE_NAME = "CryptoPreferenceSample"
    }

}
