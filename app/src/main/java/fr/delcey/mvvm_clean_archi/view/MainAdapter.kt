package fr.delcey.mvvm_clean_archi.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.delcey.mvvm_clean_archi.R

// ListAdapter would be better suited for our example, look it up
class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val data = ArrayList<PropertyUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.main_item,
                    parent,
                    false
                )
        )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) =
        holder.bind(data[position])

    fun setData(list: Collection<PropertyUiModel>) {
        data.clear()
        data.addAll(list)

        notifyDataSetChanged()
    }

    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textViewType = view.findViewById<TextView>(R.id.item_main_tv_type)
        private val textViewAddress = view.findViewById<TextView>(R.id.item_main_tv_address)

        fun bind(model: PropertyUiModel) {
            textViewType.text = model.type
            textViewAddress.text = model.mainAddress
        }
    }
}
