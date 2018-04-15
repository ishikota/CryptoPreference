package jp.ishikota.cryptopreference.preference.encrypted

import jp.ishikota.cryptopreference.cryptor.Cryptor
import jp.ishikota.cryptopreference.preference.encrypted.encoder.ByteArrayEncoder
import jp.ishikota.cryptopreference.preference.plain.PlainPreference

internal class EncryptedPreferenceImpl(
    private val criptor: Cryptor,
    private val plainPreference: PlainPreference,
    private val byteArrayEncoder: ByteArrayEncoder
): EncryptedPreference {

    override fun savePrivateString(key: String, value: String) {
        val encrypted = criptor.encrypt(key, value)
        val encoded = byteArrayEncoder.encode(encrypted)
        plainPreference.saveString(key, encoded)
    }

    override fun getPrivateString(key: String): String {
        val encoded = plainPreference.getString(key)
        val encrypted = byteArrayEncoder.decode(encoded)
        val plainByteArray = criptor.decrypt(key, encrypted)
        return String(plainByteArray)
    }

    override fun deletePrivateString(key: String) {
        plainPreference.deleteString(key)
        plainPreference.deleteIv(key)
    }

}
