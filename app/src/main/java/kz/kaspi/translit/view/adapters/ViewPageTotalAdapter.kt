package kz.kaspi.translit.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_list_stats.view.*
import kz.kaspi.translit.R
import kz.kaspi.translit.models.Statistics

private val listUserStats = mutableListOf(
    Statistics(
        pos = 1,
        name = "Bekzhan Satarkulov",
        countTime = "1860",
        countWord = "14325"
    ),
    Statistics(
        pos = 2,
        name = "Ben Postman",
        countTime = "1301",
        countWord = "10023"
    ),
    Statistics(
        pos = 3,
        name = "John Doe",
        countTime = "703",
        countWord = "7293"
    ),
    Statistics(
        pos = 4,
        name = "Rachele Olivie",
        countTime = "521",
        countWord = "6494"
    ),
    Statistics(
        pos = 5,
        name = "Anna Marshall",
        countTime = "424",
        countWord = "1890"
    ),
    Statistics(
        pos = 6,
        name = "Sabina Laura",
        countTime = "13",
        countWord = "1231"
    )
)

class UserStatsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return StatsViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return listUserStats.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StatsViewHolder).bind(listUserStats[position])
    }

    fun setItems(list: List<Statistics>) {
//      listUserStats.clear()
//        listUserStats.addAll(list)
        notifyDataSetChanged()
    }

    private class StatsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.user_list_stats, parent, false)) {

        private val name = itemView.listName
        private val word = itemView.wordListUser
        private val time = itemView.timeListUser
        private val num = itemView.listNum
        fun bind(list: Statistics) {
            num.text = position.toString()
            name.text = list.name
            word.text = list.countWord
            time.text = list.countTime
        }
    }
}
