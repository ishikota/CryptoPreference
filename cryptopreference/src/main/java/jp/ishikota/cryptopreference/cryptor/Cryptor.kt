package jp.ishikota.cryptopreference.cryptor

internal interface Cryptor {

    fun encrypt(
        alias: String,
        plain: String
    ): ByteArray

    fun decrypt(
        alias: String,
        encrypted: ByteArray
    ): ByteArray

}
