package jp.ishikota.cryptopreference.sample.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.ishikota.cryptopreference.sample.R
import kotlinx.android.synthetic.main.recyclerview_preference_entry.view.*

class PreferenceEntryRecyclerViewAdapter(private val listener: Listener):
    RecyclerView.Adapter<PreferenceEntryRecyclerViewAdapter.ViewHolder>() {

    interface Listener {

        fun onDeleteButtonClicked(entry: Contract.PreferenceEntry)

    }

    private val data = arrayListOf<Contract.PreferenceEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_preference_entry, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = data[position]
        holder.itemView.obfuscatedKeyText.text = entry.obfuscatedKey
        holder.itemView.originalKeyText.text = String.format("(%s)", entry.originalKey)
        holder.itemView.encryptedValueText.text = entry.encryptedValue.trim()
        holder.itemView.originalValueText.text = String.format("(%s)", entry.originalValue)
        holder.itemView.buttonDelete.setOnClickListener { listener.onDeleteButtonClicked(entry) }
    }

    override fun getItemCount() = data.size

    fun replaceData(newData: List<Contract.PreferenceEntry>) {
        data.clear()
        data.addAll(newData)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v)

}
