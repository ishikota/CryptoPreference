package jp.ishikota.cryptopreference.sample.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.Toast
import jp.ishikota.cryptopreference.sample.R
import kotlinx.android.synthetic.main.dialog_edit_preference.view.*

class EditPreferenceDialog: DialogFragment() {

    interface Listener {
        fun onNewPreferenceEntryReceived(key: String, value: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = setupLayout()
        return AlertDialog.Builder(context).setView(root).create()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun setupLayout() = View.inflate(context, R.layout.dialog_edit_preference, null).apply {
        buttonApply.setOnClickListener {
            val key = editKey.text.toString()
            val value = editValue.text.toString()

            if (key.isEmpty()) {
                Toast.makeText(context, R.string.dialog_edit_pref_error_empty_key, Toast.LENGTH_SHORT).show()
            } else {
                (activity as? Listener)?.onNewPreferenceEntryReceived(key, value)
                dismiss()
            }
        }
    }

}
