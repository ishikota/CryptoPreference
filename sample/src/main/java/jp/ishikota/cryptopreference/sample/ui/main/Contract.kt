package jp.ishikota.cryptopreference.sample.ui.main

interface Contract {

    // Model
    data class PreferenceEntry(
        val originalKey: String,
        val obfuscatedKey: String,
        val originalValue: String,
        val encryptedValue: String
    )

    interface View {
        fun renderPreferenceEntries(entries: List<PreferenceEntry>)
    }

    interface Presenter {
        fun onPreferenceEntriesRequested()
        fun onNewEntryInput(key: String, value: String)
        fun onEntryDeleteRequested(target: PreferenceEntry)
    }

}
