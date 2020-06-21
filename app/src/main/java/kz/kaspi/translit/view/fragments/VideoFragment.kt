package kz.kaspi.translit.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.video_fragment_main.*
import kz.kaspi.translit.R
import kz.kaspi.translit.models.ItemStorage
import kz.kaspi.translit.view.adapters.VideoAdapter

// http://bytepace.com/blog/transition2 lesson learn 19

class VideoFragment : Fragment() {

    private lateinit var adapter: VideoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        adapter = VideoAdapter({ replaceVideoFragment(it) }, ItemStorage.list)
        videoRecycler.adapter = adapter
        videoRecycler.layoutManager = LinearLayoutManager(activity?.applicationContext)

        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val s = search.text
                highlight()

                if (s?.length == 0) {
                    adapter.list = ItemStorage.list
                } else {
                    adapter.list = ItemStorage.list.filter {
                        it.subName.startsWith(
                            s.toString(),
                            true
                        ) || it.subName.contains(s.toString(), true)
                    } as ArrayList
                }
                adapter.notifyDataSetChanged()
            }
        })
    }

    private val spanHighlight by lazy {
        ForegroundColorSpan(
            ResourcesCompat.getColor(resources, R.color.selectedColorD, null)
        )
    }

    private fun highlight() {
        val s = search.text
        adapter.list.forEach { item ->

            item.subName.getSpans(0, item.subName.length, ForegroundColorSpan::class.java).forEach {
                item.subName.removeSpan(it)
            }

            item.subName.getSpans(0, item.subName.length, ForegroundColorSpan::class.java).forEach {
                item.subName.removeSpan(it)
            }

            if (item.subName.startsWith(s, true)) {
                item.subName.setSpan(spanHighlight, 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            if (item.subName.contains(s, true)) {
                val index = item.subName.toString().indexOf(s.toString(), 0, true)
                item.subName.setSpan(
                    spanHighlight,
                    index,
                    index + s.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    private fun replaceVideoFragment(url: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, (PlayerFragment.newInstance(url)))
            .commit()
    }
}