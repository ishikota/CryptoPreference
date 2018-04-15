package jp.ishikota.cryptopreference.cryptor

import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.preference.plain.PlainPreference
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

internal class CipherCryptor(
    private val secretKeyContainer: SecretKeyContainer,
    private val plainPreference: PlainPreference,
    private val algorithm: SecretKeyContainer.Algorithm,
    private val blockMode: SecretKeyContainer.BlockMode,
    private val padding: SecretKeyContainer.Padding
): Cryptor {

    override fun encrypt(
        alias: String,
        plain: String
    ): ByteArray {
        val transformation = genTransformation(algorithm, blockMode, padding)
        val secretKey = secretKeyContainer.getOrGenerateNewKey(alias, algorithm, blockMode, padding)
        val cipher = Cipher.getInstance(transformation).apply {
            init(Cipher.ENCRYPT_MODE, secretKey)
            plainPreference.saveIv(alias, iv)
        }
        return cipher.doFinal(plain.toByteArray())
    }

    override fun decrypt(
        alias: String,
        encrypted: ByteArray
    ): ByteArray {
        val transformation = genTransformation(algorithm, blockMode, padding)
        val secretKey = secretKeyContainer.getOrGenerateNewKey(alias, algorithm, blockMode, padding)
        val iv = plainPreference.getIv(alias)
        val cipher = Cipher.getInstance(transformation).apply {
            init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
        return cipher.doFinal(encrypted)
    }

    private fun genTransformation(
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ) = "${algorithm.label}/${blockMode.label}/${padding.label}"

}
