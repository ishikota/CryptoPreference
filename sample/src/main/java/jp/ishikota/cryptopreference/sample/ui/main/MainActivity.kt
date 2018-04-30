package jp.ishikota.cryptopreference.sample.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import jp.ishikota.cryptopreference.sample.R

class MainActivity : AppCompatActivity(), EditPreferenceDialog.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    override fun onNewPreferenceEntryReceived(key: String, value: String) {
        Toast.makeText(this, "$key, $value", Toast.LENGTH_SHORT).show()
    }
}
