package jp.ishikota.cryptopreference.cipher

interface CipherLogic {

    fun encrypt(alias: String, plain: String): ByteArray

    fun decrypt(alias: String, encrypted: ByteArray): String

}
