package jp.ishikota.cryptopreference.cipher

import android.util.Base64
import jp.ishikota.cryptopreference.keycontainer.SecretKeyContainer
import jp.ishikota.cryptopreference.preference.plain.PlainPreference
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

internal class CipherLogicImpl(
    private val plainPreference: PlainPreference,
    private val keyContainer: SecretKeyContainer
): CipherLogic {

    override fun encrypt(alias: String, plain: String): ByteArray {
        val secretKey = keyContainer.getKey(alias)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, secretKey)
            saveIv(alias, iv)
        }
        return cipher.doFinal(plain.toByteArray())
    }

    override fun decrypt(alias: String, encrypted: ByteArray): String {
        checkDecryptionPrecondition(alias)

        val secretKey = keyContainer.getKey(alias)
        val iv = getIv(alias)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
        return String(cipher.doFinal(encrypted))
    }

    private fun saveIv(alias: String, iv: ByteArray) = plainPreference.saveString(getIVKey(alias), encodeToBase64(iv))

    private fun getIv(alias: String) = decodeFromBase64(plainPreference.getString(getIVKey(alias)))

    private fun encodeToBase64(byteArray: ByteArray) = Base64.encodeToString(byteArray, Base64.DEFAULT)

    private fun decodeFromBase64(encoded: String) = Base64.decode(encoded, Base64.DEFAULT)

    private fun getIVKey(alias: String) = "${PREF_KEY_IV_PREFIX}_$alias"

    private fun checkDecryptionPrecondition(alias: String) {
        val keyNotSaved = !keyContainer.getAliases().contains(alias)
        val ivNotFound = !plainPreference.hasKey(getIVKey(alias))
        if (keyNotSaved) {
            throw CipherLogicException("SecretKey not found from keyContainer with alias [ $alias ]")
        } else if (ivNotFound) {
            throw CipherLogicException("IV not found from preference with alias [ $alias ]")
        }
    }

    companion object {

        private const val TAG = "CipherLogicImpl"

        private const val PREF_KEY_IV_PREFIX = "pref_key_iv_prefix"

        private const val ALGORITHM = "AES"

        private const val MODE = "CBC"

        private const val PADDING = "PKCS7Padding"

        private const val TRANSFORMATION = "$ALGORITHM/$MODE/$PADDING"

    }
}
