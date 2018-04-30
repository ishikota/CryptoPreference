package jp.ishikota.cryptopreference.cryptor

import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.CipherException
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.preference.plain.PlainPreference
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

internal class CipherCryptor(
    private val secretKeyContainer: SecretKeyContainer,
    private val plainPreference: PlainPreference,
    private val algorithm: Algorithm,
    private val blockMode: BlockMode,
    private val padding: Padding
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
        try {
            return cipher.doFinal(plain.toByteArray())
        } catch (e: Exception) {
            throw CipherException(e)
        }
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
        try {
            return cipher.doFinal(encrypted)
        } catch (e: Exception) {
            throw CipherException(e)
        }
    }

    private fun genTransformation(
        algorithm: Algorithm,
        blockMode: BlockMode,
        padding: Padding
    ) = "${algorithm.label}/${blockMode.label}/${padding.label}"

}
