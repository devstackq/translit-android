package kz.kaspi.translit.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.translate_items.view.*
import kz.kaspi.translit.R
import kz.kaspi.translit.data.entity.TranslateEntity
import kz.kaspi.translit.ui.adapters.DiffCallback

// diff util callback add, trans
class MainAdapter(
    context: Context

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mList = mutableListOf<TranslateEntity>()

    private val context: Context? = null
    private var messageList: List<TranslateEntity> = arrayListOf()

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    // create 1 view holder, - pattern translate items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // from data to bind, and set
        (holder as ViewHolder).bind(messageList[position])

        // click listener, by item position -> call other func
        holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add("Kóshіrý").setOnMenuItemClickListener {
                println("copy")
                true
            }
            contextMenu.add("Bólіsý").setOnMenuItemClickListener {
                println("share")
                true
            }
            contextMenu.add("Óshіrý").setOnMenuItemClickListener {
                // removeListener(listValue[position])
                println("reomve")
                true
            }
            contextMenu.add("Unaǵandar tіzіmіne qosý").setOnMenuItemClickListener {
                // clickListener(listValue[position])
                println("favorite")
                true
            }
        }
    }

    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        View.OnCreateContextMenuListener, RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.translate_items, parent, false
        )
    ) {
        private val latin = itemView.latin_kazakh
        private val cyril = itemView.cyrlic_kazakh

        // when onclick by item,  put value, and send Main fragment(save like favorites value, then from ModalFragment  replace -> main fragment
        fun bind(
            item: TranslateEntity
            // clickListener: (name: TranslateEntity) -> Unit
        ) {
            cyril.text = item.cyrillic
            latin.text = item.latin

            latin.setOnCreateContextMenuListener(this)
            cyril.setOnCreateContextMenuListener(this)
        }

        @SuppressLint("RestrictedApi")
        override fun onCreateContextMenu(
            menu: ContextMenu?,
            vd: View?,
            menuInfo: ContextMenu.ContextMenuInfo?

        ) {
            val popupHelper = vd?.let {
                getActivity(context)?.let { it1 ->
                    MenuPopupHelper(
                        it1,
                        it as MenuBuilder,
                        it,
                        false,
                        0
                    )
                }
            }
            popupHelper?.gravity = Gravity.TOP or Gravity.END
            popupHelper?.setForceShowIcon(true)
            popupHelper?.show()
        }
    }

    // save item, room-> list entity, adapter call ->  1etDiffValue(oldList, newList), compare data, if unique by id, add item1
    fun setDiffValue(list: List<TranslateEntity>) {
        val result: DiffUtil.DiffResult = DiffUtil.calculateDiff(DiffCallback(mList, list), false)
        result.dispatchUpdatesTo(this)
        mList.clear()
        mList.addAll(list)
        this.messageList = mList
    }
}