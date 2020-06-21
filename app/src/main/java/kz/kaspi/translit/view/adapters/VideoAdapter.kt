package kz.kaspi.translit.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.videoplayer_view.view.*
import kotlinx.android.synthetic.main.view_item_player.view.*
import kz.kaspi.translit.R
import kz.kaspi.translit.models.Movie
// import kz.kaspi.translit.models.MoviesDataSource
import kz.kaspi.translit.view.VideoPlayerView

class VideoAdapter(private val callback: ((String) -> Unit)? = null, var list: ArrayList<Movie>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    // private val items = MoviesDataSource.items
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].also { item ->
            holder.bind(item)
            holder.itemView.playVideo.setOnClickListener {
                callback?.invoke(item.previewUrl.toString())
            }
            holder.itemView.apply {
                titleVideo.text = list[position].name
                descriptionVideo.text = list[position].subName
            }
        }
    }

    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.view_item_player, parent, false)
    ) {
        private val videoPlayerView: VideoPlayerView = itemView.videoPlayerView
        fun bind(post: Movie) {
//            videoPlayerView.title.text = post.name
//            videoPlayerView.description.text = post.subName
        }
    }
}
