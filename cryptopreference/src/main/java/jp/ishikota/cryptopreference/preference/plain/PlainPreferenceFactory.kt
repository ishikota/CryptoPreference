package jp.ishikota.cryptopreference.preference.plain

import android.content.Context
import jp.ishikota.cryptopreference.preference.bytearrayencoder.Base64Encoder
import jp.ishikota.cryptopreference.preference.bytearrayencoder.ByteArrayEncoder

internal class PlainPreferenceFactory(
    private val preferenceName: String,
    private val byteArrayEncoder: ByteArrayEncoder = Base64Encoder()
) {

    fun create(context: Context, debugMode: Boolean): PlainPreference =
        PlainPreferenceImpl(context, preferenceName, byteArrayEncoder, debugMode)

}
