package kz.kaspi.translit.adapters


import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_items.view.*
import kz.kaspi.translit.R
import kz.kaspi.translit.models.TranslateData

class FavoriteAdapter(
    private val clickLis: (name: TranslateData) -> Unit,
    private val addItemListener: (name: TranslateData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val favoritesList = mutableListOf<TranslateData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        //return apply, adapter
        return FavoriteViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //from data to bind, and set
      val    adapterPosition = holder.adapterPosition
        val item = favoritesList[adapterPosition];

        (holder as FavoriteViewHolder).bind(favoritesList[position], clickLis, addItemListener)
        //remove by position, send reoved item clicklistener, and updated value share prefs

            holder.delBtn.setOnClickListener { it ->
                favoritesList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,  favoritesList.size)
        //remove item from sh pref
                clickLis(item)
                val snackbar = Snackbar
                    .make(it, "Message is deleted", Snackbar.LENGTH_SHORT)
                    .setAction("UNDO") {
                        favoritesList.add( position, item )
                        notifyItemInserted(position)
                        notifyItemRangeChanged(position,  favoritesList.size)
                        addItemListener(item)
                        val snackbar = Snackbar.make(
                            it,
                            "Message is restored!",
                            Snackbar.LENGTH_SHORT
                        )

                        snackbar.show()
                    }
                snackbar.show()
            }
    }

    //send Items, view Holder
    fun setItems(item: MutableList<TranslateData>) {
        favoritesList.run {
            clear()
            addAll(item)
        }
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.favorite_items, parent, false)) {

        private val latin = itemView.favLatin
        private val cyril = itemView.favCyrilic
        val delBtn: ImageButton = itemView.delete_favorite

        fun bind(
            post: TranslateData,
            clickLis: (name: TranslateData) -> Unit,
            addItemListener: (name: TranslateData) -> Unit
        ) {
            cyril.text = post.inputValue
            latin.text = post.resultValue
            }
        }
    }