package jp.ishikota.cryptopreference.helper

import jp.ishikota.cryptopreference.cipher.CipherLogic
import jp.ishikota.cryptopreference.cipher.CipherLogicException

class TestCipherLogic: CipherLogic {

    private val aliases = arrayListOf<String>()

    override fun encrypt(alias: String, plain: String): ByteArray {
        aliases.add(alias)
        return "$alias-$plain".toByteArray()
    }

    override fun decrypt(alias: String, encrypted: ByteArray): String {
        if (!aliases.contains(alias)) {
            throw CipherLogicException("Not encrypted with alias [ $alias ] yet.")
        }
        val s = String(encrypted)
        return s.substring(5)
    }
}
