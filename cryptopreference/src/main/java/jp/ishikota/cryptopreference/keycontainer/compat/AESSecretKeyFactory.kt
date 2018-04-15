package jp.ishikota.cryptopreference.keycontainer.compat

import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.keycontainer.SecretKeyFactory
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

internal class AESSecretKeyFactory(private val secret: ByteArray): SecretKeyFactory {

    override fun isSupported(
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ) = algorithm == SecretKeyContainer.Algorithm.AES &&
        blockMode == SecretKeyContainer.BlockMode.CBC &&
        padding == SecretKeyContainer.Padding.PKCS7


    override fun create(
        alias: String,
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ): SecretKey = SecretKeySpec(secret, ALGORITHM)

    companion object {

        private const val ALGORITHM = "AES"

    }
}
