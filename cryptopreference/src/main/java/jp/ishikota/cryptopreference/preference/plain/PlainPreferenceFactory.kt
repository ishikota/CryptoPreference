package jp.ishikota.cryptopreference.preference.plain

import android.content.Context
import jp.ishikota.cryptopreference.preference.encrypted.encoder.Base64Encoder
import jp.ishikota.cryptopreference.preference.encrypted.encoder.ByteArrayEncoder

internal class PlainPreferenceFactory(
    private val byteArrayEncoder: ByteArrayEncoder = Base64Encoder()
) {

    fun create(context: Context, debugMode: Boolean): PlainPreference =
        PlainPreferenceImpl(context, PREF_NAME, byteArrayEncoder, debugMode)

    companion object {

        private const val PREF_NAME = "CryptoPreference"

    }

}
