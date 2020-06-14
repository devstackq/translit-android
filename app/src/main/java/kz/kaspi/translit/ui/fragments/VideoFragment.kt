package kz.kaspi.translit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.video_fragment_main.*
import kz.kaspi.translit.R
import kz.kaspi.translit.ui.adapters.VideoAdapter

//http://bytepace.com/blog/transition2 lesson learn

class VideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      return inflater.inflate(R.layout.video_fragment_main, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        videoRecycler.apply {
            adapter = VideoAdapter(callback = { replaceVideoFragment(it) })
            layoutManager = linearLayoutManager
        }
    }

    private fun replaceVideoFragment(url: String) {
    requireActivity().supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, (PlayerFragment.newInstance(url) ))
        .commit()
    }
}

