package jp.ishikota.cryptopreference

import android.content.Context
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainerFactory
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreference
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreferenceFactory
import jp.ishikota.cryptopreference.preference.obfuscator.Sha256Obfuscator
import jp.ishikota.cryptopreference.preference.plain.PlainPreferenceFactory

class CryptoPreference {

    companion object {

        private val encryptedPreferenceFactory = EncryptedPreferenceFactory()

        fun create(context: Context,
            algorithm: Algorithm,
            blockMode: BlockMode,
            padding: Padding): EncryptedPreference {

            Initializer.checkInitialization()?.let { throw it }

            val plainPreferenceFactory = PlainPreferenceFactory(Initializer.preferenceName!!, keyObfuscator = Initializer.keyObfuscator)
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

            var keyObfuscator: KeyObfuscator = Sha256Obfuscator()

            private fun useCompat() = android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M

            internal fun checkInitialization(): Exception? {
                val errorMessages = arrayListOf<String>()
                if (useCompat() && secretKeyForCompat == null) {
                    errorMessages.add("secretKeyForCompat is not set but current device is prior to android.os.Build.VERSION_CODES.M")
                }
                if (preferenceName == null) {
                    errorMessages.add("preferenceName is not set.")
                }
                return if (errorMessages.isEmpty()) {
                    null
                } else {
                    InitializationException(errorMessages.toString())
                }
            }
        }

    }
}

class InitializationException(msg: String): RuntimeException(msg)

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
