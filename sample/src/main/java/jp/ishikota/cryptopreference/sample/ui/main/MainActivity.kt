package jp.ishikota.cryptopreference.sample.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import jp.ishikota.cryptopreference.Algorithm
import jp.ishikota.cryptopreference.BlockMode
import jp.ishikota.cryptopreference.CryptoPreference
import jp.ishikota.cryptopreference.Padding
import jp.ishikota.cryptopreference.sample.R
import jp.ishikota.cryptopreference.sample.ui.CryptoPreferenceSampleApp

class MainActivity : AppCompatActivity(),
    Contract.View,
    EditPreferenceDialog.Listener {

    private lateinit var presenter: Contract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val encryptedPref = CryptoPreference.create(this, Algorithm.AES, BlockMode.CBC, Padding.PKCS7)
        presenter = MainPresenter(this, encryptedPref, genLibraryInternalPreference(), genAppPreference())

        presenter.onPreferenceEntriesRequested()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.edit -> {
                EditPreferenceDialog().show(supportFragmentManager, "EditPreferenceDialog")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun renderPreferenceEntries(entries: List<Contract.PreferenceEntry>) {
        Log.d("TODO", entries.toString())
    }

    override fun onNewPreferenceEntryReceived(key: String, value: String) {
        presenter.onNewEntryInput(key, value)
    }

    private fun genLibraryInternalPreference(): SharedPreferences =
        getSharedPreferences(CryptoPreferenceSampleApp.CRYPTO_PREFERENCE_NAME, Context.MODE_PRIVATE)

    private fun genAppPreference(): SharedPreferences =
        getSharedPreferences(CryptoPreferenceSampleApp.APP_PREFERENCE_NAME, Context.MODE_PRIVATE)

}
