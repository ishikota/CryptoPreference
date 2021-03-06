package jp.ishikota.cryptopreference.keycontainer.androidkeystore

import android.annotation.TargetApi
import android.os.Build
import android.support.annotation.VisibleForTesting
import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.keyfactory.SecretKeyFactory
import jp.ishikota.cryptopreference.keyfactory.aes.androidkeystore.AESSecretKeyFactory
import java.security.KeyStore
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
internal class AndroidKeyStoreContainer : SecretKeyContainer {

    private val keystore: KeyStore

    private val keyFactories = listOf(AESSecretKeyFactory())

    init {
        keystore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
            load(null)
        }
    }

    override fun getOrGenerateNewKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): SecretKey {
        val keyAlias = genKeyAlias(alias, algorithm, blockMode, padding)
        return if (keystore.containsAlias(keyAlias)) {
            val entry = (keystore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry)
            entry.secretKey
        } else {
            val keyFactory = getKeyFactory(algorithm, blockMode, padding)
            keyFactory.create(keyAlias, algorithm, blockMode, padding)
        }
    }

    override fun deleteKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ) {
        keystore.deleteEntry(genKeyAlias(alias, algorithm, blockMode, padding))
    }

    override fun hasKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): Boolean =
        keystore.containsAlias(genKeyAlias(alias, algorithm, blockMode, padding))

    private fun getKeyFactory(
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding): SecretKeyFactory {
        val supportedKeyFactories = keyFactories.filter { it.isSupported(algorithm, blockMode, padding) }
        return when {
            supportedKeyFactories.isEmpty() ->
                throw SecretKeyContainer.TransformationNotSupportedException(algorithm, blockMode, padding)
            supportedKeyFactories.size > 1 ->
                throw SecretKeyContainer.TooManySecretKeyFactoryException(supportedKeyFactories, algorithm, blockMode, padding)
            else ->
                supportedKeyFactories.first()
        }
    }

    private fun genKeyAlias(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ) = "${alias}_${algorithm.label}_${blockMode.label}_${padding.label}"


    @VisibleForTesting
    fun clearKeys() {
        keystore.aliases().toList().forEach { keystore.deleteEntry(it) }
    }

    companion object {

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    }
}
