package kz.kaspi.translit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.video_items.view.*
import kotlinx.android.synthetic.main.videoplayer_view.view.*
import kz.kaspi.translit.R
import kz.kaspi.translit.models.MoviesData

class VideoAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val movies = mutableListOf<MoviesData>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        //return apply, adapter
        context = parent.context
        return VideoViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoViewHolder).bind(movies[position])
    }

    //send Items, view Holder
    fun setItems(item: List<MoviesData>) {
        movies.addAll(item)
        notifyDataSetChanged()
    }



    inner class VideoViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.video_items, parent, false)) {

        fun bind(movie: MoviesData){
            val video  = itemView.videoPlayerView
            video.playVideo.setOnClickListener{
                video.play(movie.movieURL)
                video.titleVideo.text = movie.movieTitle
            }
          }
        }
    }