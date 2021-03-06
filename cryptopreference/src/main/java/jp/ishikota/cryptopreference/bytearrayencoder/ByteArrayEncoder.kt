package jp.ishikota.cryptopreference.bytearrayencoder

interface ByteArrayEncoder {

    fun encode(byteArray: ByteArray): String

    fun decode(encoded: String): ByteArray

}
