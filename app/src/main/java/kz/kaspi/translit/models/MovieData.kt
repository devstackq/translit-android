package kz.kaspi.translit.models

import android.text.Spannable
import android.text.SpannableString

data class Movie(
    val name: Spannable,
    val subName: Spannable,
    val previewUrl: Spannable,
    val thumbID: Spannable
) {
    constructor(name: String, subName: String, previewUrl: String, thumbID: String) : this(
        SpannableString(name),
        SpannableString(subName),
        SpannableString(previewUrl),
        SpannableString(thumbID)
    )
}

class ItemStorage {
    companion object {
        val list: ArrayList<Movie> =
            ArrayList<Movie>().apply {
                add(
                    Movie(
                        "Body Building tutorial",
                        "Sorry, I'm not sure what error you mean could you be a bit more specific? If you're trying to clear and EditText, you can simply cal",
                        "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4",
                        ""
                    )
                )
                add(
                    Movie(
                        "Animation movie",
                        "This error means that you're using the adapter before initializing it. Here it's probably because your store's callback is asynchronous.",
                        "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                        ""
                    )
                )
                add(
                    Movie(
                        "Constructor toys",
                        "adapter.list = stores ; adapter.notifyDataSetChanged() This tells the RecyclerView to re-draw all of the items in the adapter ",
                        "http://techslides.com/demos/sample-videos/small.mp4",
                        ""
                    )
                )
            }
    }
}