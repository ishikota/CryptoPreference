package jp.ishikota.cryptopreference.keycontainer.androidkeystore

import android.annotation.TargetApi
import android.os.Build
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.keycontainer.SecretKeyFactory
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
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
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
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ) {
        keystore.deleteEntry(genKeyAlias(alias, algorithm, blockMode, padding))
    }

    override fun hasKey(
        alias: String,
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ): Boolean =
        keystore.containsAlias(genKeyAlias(alias, algorithm, blockMode, padding))

    private fun getKeyFactory(
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding): SecretKeyFactory {
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
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ) = "${alias}_${algorithm.label}_${blockMode.label}_${padding.label}"

    companion object {

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    }
}
