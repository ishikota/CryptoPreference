package jp.ishikota.cryptopreference.preference.plain

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

internal class PlainPreferenceImpl(private val context: Context, private val debugMode: Boolean = false): PlainPreference {

    override fun saveString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
        if (debugMode) {
            Log.d(TAG, "saved string [ $value ] with key [ $key ]")
        }
    }

    override fun getString(key: String): String = pref.getString(key, DEFAULT_STRING)

    override fun delete(key: String) {
        pref.edit().remove(key).apply()
        if (debugMode) {
            Log.d(TAG, "deleted string which saved with key [ $key ]")
        }
    }

    override fun hasKey(key: String): Boolean = pref.contains(key)

    override fun clear() {
        pref.all.keys.forEach { delete(it) }
    }

    private val pref: SharedPreferences
        get() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {

        private const val TAG = "PlainPreferenceImpl"

        private const val PREF_NAME = "PlainPreference"

        private const val DEFAULT_STRING = ""

    }
}
