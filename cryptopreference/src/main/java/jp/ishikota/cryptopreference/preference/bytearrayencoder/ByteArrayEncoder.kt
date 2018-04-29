package jp.ishikota.cryptopreference.preference.bytearrayencoder

interface ByteArrayEncoder {

    fun encode(byteArray: ByteArray): String

    fun decode(encoded: String): ByteArray

}
