package jp.ishikota.cryptopreference.preference.plain

internal interface PlainPreference {

    fun saveString(key: String, value: String)

    fun getString(key: String): String

    fun deleteString(key: String)

    fun hasStringKey(key: String): Boolean

    fun saveIv(keyAlias: String, iv: ByteArray)

    fun getIv(keyAlias: String): ByteArray

    fun deleteIv(keyAlias: String)

    fun hasIvKey(keyAlias: String): Boolean

    fun clear()

}
