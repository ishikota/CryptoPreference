package jp.ishikota.cryptopreference.preference.encrypted.encoder

import android.util.Base64

class Base64Encoder: ByteArrayEncoder {

    override fun encode(byteArray: ByteArray): String =
        Base64.encodeToString(byteArray, Base64.DEFAULT)

    override fun decode(encoded: String) = Base64.decode(encoded, Base64.DEFAULT)
}