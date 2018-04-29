package jp.ishikota.cryptopreference.preference.plain

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import jp.ishikota.cryptopreference.preference.bytearrayencoder.ByteArrayEncoder

internal class PlainPreferenceImpl(
    private val context: Context,
    private val preferenceName: String,
    private val byteArrayEncoder: ByteArrayEncoder,
    private val debugMode: Boolean
): PlainPreference {

    override fun saveString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
        if (debugMode) {
            Log.d(TAG, "saved string [ $value ] with key [ $key ]")
        }
    }

    override fun getString(key: String): String = pref.getString(key, DEFAULT_STRING)

    override fun deleteString(key: String) {
        pref.edit().remove(key).apply()
        if (debugMode) {
            Log.d(TAG, "deleted string which saved with key [ $key ]")
        }
    }

    override fun hasStringKey(key: String): Boolean = pref.contains(key)

    override fun saveIv(keyAlias: String, iv: ByteArray) {
        val encoded = byteArrayEncoder.encode(iv)
        saveString(genIvKey(keyAlias), encoded)
    }

    override fun getIv(keyAlias: String): ByteArray {
        val encoded = getString(genIvKey(keyAlias))
        return byteArrayEncoder.decode(encoded)
    }

    override fun deleteIv(keyAlias: String) {
        deleteString(genIvKey(keyAlias))
    }

    override fun hasIvKey(keyAlias: String) = pref.contains(genIvKey(keyAlias))

    override fun clear() {
        pref.all.keys.forEach { deleteString(it) }
    }

    private fun genIvKey(keyAlias: String) = "${IV_KEY_PREFIX}_$keyAlias"

    private val pref: SharedPreferences
        get() = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    companion object {

        private const val TAG = "CryptoPreference"

        private const val DEFAULT_STRING = ""

        private const val IV_KEY_PREFIX = "IV_"

    }
}
