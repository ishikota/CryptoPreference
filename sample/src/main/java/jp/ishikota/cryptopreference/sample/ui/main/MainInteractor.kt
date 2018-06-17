package jp.ishikota.cryptopreference.sample.ui.main

import android.content.SharedPreferences
import jp.ishikota.cryptopreference.obfuscator.Sha256Obfuscator
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreference

class MainInteractor(
    private val encryptedPreference: EncryptedPreference,
    private val libraryInternalPreference: SharedPreferences,
    preferenceForKeyRecorder: SharedPreferences
): Contract.Interactor {

    private val obfuscator = Sha256Obfuscator()

    private val keyRecorder = PreferenceKeyRecorder(preferenceForKeyRecorder)

    override fun fetchEntries(): List<Contract.PreferenceEntry> {
        return keyRecorder.getKeys().map { keyToEntry(it) }
    }

    override fun saveEntry(key: String, value: String) {
        encryptedPreference.savePrivateString(key, value)
        keyRecorder.recordNewKey(key)
    }

    override fun deleteEntry(key: String) {
        encryptedPreference.deletePrivateString(key)
        keyRecorder.deleteKey(key)
    }

    private fun keyToEntry(originalKey: String): Contract.PreferenceEntry {
        val obfuscatedKey = obfuscator.obfuscate(originalKey)
        val originalValue = encryptedPreference.getPrivateString(originalKey, "failed to fetch value with key=$originalKey.")
        val encryptedValue = libraryInternalPreference.getString(obfuscatedKey, "error")
        return Contract.PreferenceEntry(
            originalKey,
            obfuscatedKey,
            originalValue,
            encryptedValue
        )
    }

    private class PreferenceKeyRecorder(private val preference: SharedPreferences) {

        fun recordNewKey(key: String) {
            var entries = preference.getString(KEY_ENTRY_LIST, "").split(SEPARATOR).filter { !it.isEmpty() }
            if (!entries.contains(key)) {
                entries += listOf(key)
            }
            preference.edit().putString(KEY_ENTRY_LIST, entries.joinToString(SEPARATOR)).apply()
        }

        fun deleteKey(key: String) {
            val entries = getKeys()
            val deleted = entries.filter { it != key }.joinToString(SEPARATOR)
            preference.edit().putString(KEY_ENTRY_LIST, deleted).apply()
        }

        fun getKeys(): List<String> {
            val entriesData = preference.getString(KEY_ENTRY_LIST, "")
            return if (entriesData.isEmpty()) {
                emptyList()
            } else {
                entriesData.split(SEPARATOR)
            }
        }

        companion object {
            private const val KEY_ENTRY_LIST = "KEY_ENTRY_LIST"
            private const val SEPARATOR = "jpishikotacryptopreference"
        }


    }
}
