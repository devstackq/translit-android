package kz.kaspi.translit.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import kz.kaspi.translit.data.entity.TranslateEntity

class DiffCallback(
    private val oldList: List<TranslateEntity>,
    private val newList: List<TranslateEntity>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMsg = oldList[oldItemPosition]
        val newMsg = newList[newItemPosition]

        if (oldMsg.id != newMsg.id) {
            return false
        }
        return true
    }
}