package jp.ishikota.cryptopreference.preference.plain

import android.content.Context
import jp.ishikota.cryptopreference.KeyObfuscator
import jp.ishikota.cryptopreference.bytearrayencoder.Base64Encoder
import jp.ishikota.cryptopreference.bytearrayencoder.ByteArrayEncoder
import jp.ishikota.cryptopreference.obfuscator.Sha256Obfuscator

internal class PlainPreferenceFactory(
    private val preferenceName: String,
    private val byteArrayEncoder: ByteArrayEncoder = Base64Encoder(),
    private val keyObfuscator: KeyObfuscator = Sha256Obfuscator()
) {

    fun create(context: Context, debugMode: Boolean): PlainPreference =
        PlainPreferenceImpl(context, preferenceName, byteArrayEncoder, keyObfuscator, debugMode)

}
