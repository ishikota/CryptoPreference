package jp.ishikota.cryptopreference.preference.encrypted

import jp.ishikota.cryptopreference.cipher.CipherLogic
import jp.ishikota.cryptopreference.preference.encrypted.encoder.ByteArrayEncoder
import jp.ishikota.cryptopreference.preference.plain.PlainPreference

internal class EncryptedPreferenceImpl(
    private val cipher: CipherLogic,
    private val plainPreference: PlainPreference,
    private val byteArrayEncoder: ByteArrayEncoder
): EncryptedPreference {

    override fun saveString(key: String, plainText: String) {
        val encrypted = cipher.encrypt(key, plainText)
        val encoded = byteArrayEncoder.encode(encrypted)
        plainPreference.saveString(key, encoded)
    }

    // return empty string if failed to decrypt
    override fun getString(key: String): String {
        return if (!plainPreference.hasKey(key)) {
            ""
        } else {
            val encoded = plainPreference.getString(key)
            val encrypted = byteArrayEncoder.decode(encoded)
            cipher.decrypt(key, encrypted)
        }
    }
}
