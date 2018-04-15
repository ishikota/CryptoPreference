package jp.ishikota.cryptopreference.cryptor

import android.util.Base64
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
            saveIv(alias, iv)
        }
        return cipher.doFinal(plain.toByteArray())
    }

    override fun decrypt(
        alias: String,
        encrypted: ByteArray
    ): ByteArray {
        val transformation = genTransformation(algorithm, blockMode, padding)
        val secretKey = secretKeyContainer.getOrGenerateNewKey(alias, algorithm, blockMode, padding)
        val iv = getIv(alias)
        val cipher = Cipher.getInstance(transformation).apply {
            init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
        return cipher.doFinal(encrypted)
    }

    private fun saveIv(alias: String, iv: ByteArray) = plainPreference.saveString(getIVKey(alias), encodeToBase64(iv))

    private fun getIv(alias: String) = decodeFromBase64(plainPreference.getString(getIVKey(alias)))

    private fun encodeToBase64(byteArray: ByteArray) = Base64.encodeToString(byteArray, Base64.DEFAULT)

    private fun decodeFromBase64(encoded: String) = Base64.decode(encoded, Base64.DEFAULT)

    private fun getIVKey(alias: String) = "${PREF_KEY_IV_PREFIX}_$alias"


    private fun genTransformation(
        algorithm: SecretKeyContainer.Algorithm,
        blockMode: SecretKeyContainer.BlockMode,
        padding: SecretKeyContainer.Padding
    ) = "${algorithm.label}/${blockMode.label}/${padding.label}"

    companion object {

        private const val PREF_KEY_IV_PREFIX = "pref_key_iv_prefix"

    }
}
