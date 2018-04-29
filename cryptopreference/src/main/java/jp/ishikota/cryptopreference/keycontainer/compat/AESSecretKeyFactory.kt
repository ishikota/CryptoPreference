package jp.ishikota.cryptopreference.keycontainer.compat

import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keycontainer.SecretKeyFactory
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

internal class AESSecretKeyFactory(private val secret: ByteArray): SecretKeyFactory {

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

        private const val ALGORITHM = "AES"

    }
}
