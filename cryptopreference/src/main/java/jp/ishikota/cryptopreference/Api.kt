package jp.ishikota.cryptopreference

import android.content.Context
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainerFactory
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreference
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreferenceFactory
import jp.ishikota.cryptopreference.preference.plain.PlainPreferenceFactory

class CryptoPreference {

    companion object {

        private val encryptedPreferenceFactory = EncryptedPreferenceFactory()

        fun create(context: Context,
            algorithm: Algorithm,
            blockMode: BlockMode,
            padding: Padding): EncryptedPreference {

            if (!Initializer.isInitialized()) {
                throw InitializationException()
            }

            val plainPreferenceFactory = PlainPreferenceFactory(Initializer.preferenceName!!)
            val secretKeyContainer = SecretKeyContainerFactory(Initializer.secretKeyForCompat).create()
            val plainPreference = plainPreferenceFactory.create(context, Initializer.debugMode)
            return encryptedPreferenceFactory.create(
                secretKeyContainer,
                plainPreference,
                algorithm,
                blockMode,
                padding
            )
        }

    }

    class Initializer {

        companion object {

            var secretKeyForCompat: ByteArray? = null

            var preferenceName: String? = null

            var debugMode: Boolean = false

            internal fun isInitialized() =
                secretKeyForCompat != null &&
                    preferenceName != null
        }

    }
}

class InitializationException: RuntimeException()

enum class Algorithm(val label: String) {
    AES("AES")
}

enum class BlockMode(val label: String) {
    CBC("CBC")
}

enum class Padding(val label: String) {
    PKCS7("PKCS7Padding")
}

interface KeyObfuscator {

    fun obfuscate(rawKey: String): String

}
