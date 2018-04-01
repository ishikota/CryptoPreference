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

    override fun getKey(alias: String): SecretKey {
        if (keystore.containsAlias(alias)) {
            return (keystore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
        } else {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
            keyGenerator.init(KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build())
            return keyGenerator.generateKey()
        }
    }

    override fun deleteKey(alias: String) {
        keystore.deleteEntry(alias)
    }

    override fun getAliases(): List<String> = keystore.aliases().toList()

    companion object {

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    }
}
