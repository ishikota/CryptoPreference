package jp.ishikota.cryptopreference.preference.encrypted

interface EncryptedPreference {

    fun saveString(key: String, plainText: String)

    fun getString(key: String): String

}
