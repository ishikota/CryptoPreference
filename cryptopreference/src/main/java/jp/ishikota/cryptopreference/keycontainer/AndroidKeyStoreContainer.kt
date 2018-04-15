package jp.ishikota.cryptopreference.keycontainer

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
class AndroidKeyStoreContainer : SecretKeyContainer {

    private val keystore: KeyStore

    init {
        keystore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
        }
    }

    override fun getOrGenerateNewKey(alias: String, algorithm: String): SecretKey {
        val keyAlias = genKeyAlias(alias, algorithm)
        if (keystore.containsAlias(keyAlias)) {
            return (keystore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey
        } else {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
            keyGenerator.init(KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build())
            return keyGenerator.generateKey()
        }
    }

    override fun deleteKey(alias: String, algorithm: String) {
        keystore.deleteEntry(genKeyAlias(alias, algorithm))
    }

    override fun hasKey(alias: String, algorithm: String): Boolean =
        keystore.containsAlias(genKeyAlias(alias, algorithm))

    private fun genKeyAlias(alias: String, algorithm: String) = "${alias}_$algorithm"

    companion object {

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    }
}
