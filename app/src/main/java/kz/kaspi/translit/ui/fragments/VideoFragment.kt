package kz.kaspi.translit.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_video.*
import kz.kaspi.translit.R
import kz.kaspi.translit.adapters.VideoAdapter
import kz.kaspi.translit.models.MoviesData

class VideoFragment : Fragment() {

    private val videoAdapter = VideoAdapter()
    private val movies = ArrayList<MoviesData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getVideoList()

        val linearLayoutManager = LinearLayoutManager(activity?.baseContext)
        videoRecycler?.layoutManager = linearLayoutManager
        videoRecycler?.adapter = videoAdapter



    }
//   override  fun playButtonClick(v: View?) {
//        videoView.start()
//        seekBar.isIndeterminate = false
////            seekBar.setCancelable(true)
////            seekBar.show()
//        if (videoView.isPlaying) {
//            playVideo.visibility = ImageView.INVISIBLE
//            pauseVideo.visibility = ImageView.INVISIBLE
//        } else {
//            pauseVideo.visibility = ImageView.VISIBLE
//            playVideo.visibility = ImageView.VISIBLE
//        }
//    }
    private fun getVideoList() {
            movies.add(
                MoviesData(
                    "Lesson 10, how to train?",
                    "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"
                )
            )
            movies.add(
                MoviesData(
                    "Big bunny duck",
                    "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
                )
            )
           movies.add(
                MoviesData(
                   "Sample robots video",
                   "http://techslides.com/demos/sample-videos/small.mp4"
              )
           )
            videoAdapter.setItems(movies)
        }
}