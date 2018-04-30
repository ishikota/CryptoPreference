package jp.ishikota.cryptopreference.sample.ui.main

import android.content.SharedPreferences
import jp.ishikota.cryptopreference.preference.encrypted.EncryptedPreference
import jp.ishikota.cryptopreference.preference.obfuscator.Sha256Obfuscator

class MainPresenter(
    private val view: Contract.View,
    private val encryptedPreference: EncryptedPreference,
    private val libraryInternalPreference: SharedPreferences,
    appPreference: SharedPreferences
) : Contract.Presenter {

    private val obfuscator = Sha256Obfuscator()

    private val entryRecorder = EntryRecorder(appPreference)

    override fun onPreferenceEntriesRequested() {
        reflectNewEntryToUI()
    }


    override fun onNewEntryInput(key: String, value: String) {
        encryptedPreference.savePrivateString(key, value)
        entryRecorder.recordNewEntry(key)

        reflectNewEntryToUI()
    }

    override fun onEntryDeleteRequested(target: Contract.PreferenceEntry) {
        encryptedPreference.deletePrivateString(target.originalKey)
        entryRecorder.deleteEntry(target.originalKey)

        reflectNewEntryToUI()
    }


    private fun reflectNewEntryToUI() {
        val entries = entryRecorder.getEntries().map { keyToEntry(it) }
        view.renderPreferenceEntries(entries)
    }

    private fun keyToEntry(originalKey: String): Contract.PreferenceEntry {
        val obfuscatedKey = obfuscator.obfuscate(originalKey)
        val originalValue = encryptedPreference.getPrivateString(originalKey)
        val encryptedValue = libraryInternalPreference.getString(obfuscatedKey, "error")
        return Contract.PreferenceEntry(
            originalKey,
            obfuscatedKey,
            originalValue,
            encryptedValue
        )
    }

    private class EntryRecorder(private val preference: SharedPreferences) {

        fun recordNewEntry(key: String) {
            var entries = preference.getString(KEY_ENTRY_LIST, "").split(SEPARATOR).filter { !it.isEmpty() }
            if (!entries.contains(key)) {
                entries += listOf(key)
            }
            preference.edit().putString(KEY_ENTRY_LIST, entries.joinToString(SEPARATOR)).apply()
        }

        fun deleteEntry(key: String) {
            val entries = getEntries()
            val deleted = entries.filter { it != key }.joinToString(SEPARATOR)
            preference.edit().putString(KEY_ENTRY_LIST, deleted).apply()
        }

        fun getEntries(): List<String> {
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
