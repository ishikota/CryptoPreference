package jp.ishikota.cryptopreference.preference.encrypted.encoder

interface ByteArrayEncoder {

    fun encode(byteArray: ByteArray): String

    fun decode(encoded: String): ByteArray

}
