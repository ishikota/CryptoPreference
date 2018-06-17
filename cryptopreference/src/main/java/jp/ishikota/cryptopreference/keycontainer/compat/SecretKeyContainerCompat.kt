package jp.ishikota.cryptopreference.keycontainer.compat

import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.keyfactory.SecretKeyFactory
import jp.ishikota.cryptopreference.keyfactory.aes.compat.AESSecretKeyFactory
import java.security.Key

internal class SecretKeyContainerCompat(private val secret: ByteArray): SecretKeyContainer {

    private val keyFactories = listOf(AESSecretKeyFactory(secret))

    override fun getOrGenerateNewKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): Key {
        val keyFactory = getKeyFactory(algorithm, blockMode, padding)
        return keyFactory.create(alias, algorithm, blockMode, padding)
    }

    override fun deleteKey(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ) {
        /* do nothing because this container does not save secret key*/
    }

    override fun hasKey(alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): Boolean = getSupportedKeyFactories(algorithm, blockMode, padding).isNotEmpty()

    private fun getKeyFactory(
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding): SecretKeyFactory {
        val supportedKeyFactories = getSupportedKeyFactories(algorithm, blockMode, padding)
        return when {
            supportedKeyFactories.isEmpty() ->
                throw SecretKeyContainer.TransformationNotSupportedException(algorithm, blockMode, padding)
            supportedKeyFactories.size > 1 ->
                throw SecretKeyContainer.TooManySecretKeyFactoryException(supportedKeyFactories, algorithm, blockMode, padding)
            else ->
                supportedKeyFactories.first()
        }
    }

    private fun getSupportedKeyFactories(
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding) = keyFactories.filter {
        it.isSupported(algorithm, blockMode, padding)
    }
}
