package jp.ishikota.cryptopreference.preference.encrypted

interface EncryptedPreference {

    fun savePrivateString(key: String, value: String)

    fun getPrivateString(key: String, default: String): String

    fun deletePrivateString(key: String)

}
