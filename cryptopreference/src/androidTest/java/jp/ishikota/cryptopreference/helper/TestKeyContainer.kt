package jp.ishikota.cryptopreference.helper

import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class TestKeyContainer(private val aliases: List<String>): SecretKeyContainer {

    private val keys: HashMap<String, SecretKey> = HashMap()

    init {
        aliases.forEach {
            keys.put(it, generateKey())
        }
    }

    override fun getKey(alias: String): SecretKey = keys.get(alias)!!

    override fun deleteKey(alias: String) {
        keys.remove(alias)
    }

    override fun getAliases(): List<String> = aliases

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(KEY_SIZE, RANDOM)
        return keyGenerator.generateKey()
    }

    companion object {

        private const val KEY_SIZE = 256

        private val RANDOM = SecureRandom()

    }
}
