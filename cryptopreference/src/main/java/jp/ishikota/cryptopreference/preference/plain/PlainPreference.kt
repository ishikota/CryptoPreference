package jp.ishikota.cryptopreference.preference.plain

internal interface PlainPreference {

    fun saveString(key: String, value: String)

    fun getString(key: String): String

    fun delete(key: String)

    fun hasKey(key: String): Boolean

    fun clear()

}
