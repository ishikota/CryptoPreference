package jp.ishikota.cryptopreference.sample

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.Toast
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

            val errorMsg = genErrorMessage(key, value)
            if (errorMsg == null) {
                (activity as? Listener)?.onNewPreferenceEntryReceived(key, value)
                dismiss()
            } else {
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun genErrorMessage(key: String, value: String): Int? = when {
        key.isEmpty() && value.isEmpty() -> R.string.dialog_edit_pref_error_empty_key_value
        key.isEmpty() -> R.string.dialog_edit_pref_error_empty_key
        value.isEmpty() -> R.string.dialog_edit_pref_error_empty_value
        else -> null
    }

}
