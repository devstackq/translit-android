package kz.kaspi.translit.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.translate_yandex_items.view.*
import kz.kaspi.translit.R
import kz.kaspi.translit.models.YandexDataAdapter

class YandexAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val transYandex = mutableListOf<YandexDataAdapter>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        //return apply, adapter
        context = parent.context
        return YandexViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return transYandex.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as YandexViewHolder).bind(transYandex[position])
    }
    //send Items, view Holder
    fun setItems(item: MutableList<YandexDataAdapter>) {

        transYandex.run {
            clear()
            addAll(item)
        }
        notifyDataSetChanged()

    }
    inner class YandexViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.translate_yandex_items, parent, false)) {
        val transInput = itemView.input_lng
        val yandexRes = itemView.trans_res

        fun bind(item: YandexDataAdapter){
            transInput.text = item.input
            yandexRes.text = item.result
            }
        }
}