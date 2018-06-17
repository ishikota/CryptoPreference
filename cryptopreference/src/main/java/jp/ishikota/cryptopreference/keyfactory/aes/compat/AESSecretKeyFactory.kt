package jp.ishikota.cryptopreference.keyfactory.aes.compat

import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keyfactory.SecretKeyFactory
import java.security.InvalidKeyException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

internal class AESSecretKeyFactory(private val secret: ByteArray): SecretKeyFactory {

    init {
        if (secret.size != SUPPORTED_KEY_SIZE) {
            throw InvalidKeyException("secretKeyForCompat must be $SUPPORTED_KEY_SIZE characters. Your key size is ${secret.size}")
        }
    }


    override fun isSupported(
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ) = algorithm == Algorithm.AES &&
        blockMode == BlockMode.CBC &&
        padding == Padding.PKCS7


    override fun create(
        alias: String,
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ): SecretKey = SecretKeySpec(secret, ALGORITHM)

    companion object {
        private const val SUPPORTED_KEY_SIZE = 16
        private const val ALGORITHM = "AES"

    }
}
