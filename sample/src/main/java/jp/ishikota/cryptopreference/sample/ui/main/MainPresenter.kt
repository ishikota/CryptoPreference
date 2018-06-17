package jp.ishikota.cryptopreference.sample.ui.main

class MainPresenter(
    private val view: Contract.View,
    private val interactor: Contract.Interactor
) : Contract.Presenter {

    override fun onPreferenceEntriesRequested() {
        view.renderPreferenceEntries(interactor.fetchEntries())
    }

    override fun onNewEntryInput(key: String, value: String) {
        interactor.saveEntry(key, value)
        view.renderPreferenceEntries(interactor.fetchEntries())
    }

    override fun onEntryDeleteRequested(target: Contract.PreferenceEntry) {
        interactor.deleteEntry(target.originalKey)
        view.renderPreferenceEntries(interactor.fetchEntries())
    }
}
