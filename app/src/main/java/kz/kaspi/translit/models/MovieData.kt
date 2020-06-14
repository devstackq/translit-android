package kz.kaspi.translit.models

data class Movie(
    val name: String? =null,
    val subName: String? = null,
    val previewUrl: String,
    val thumbID: String?=null
)

object MoviesDataSource {
    val items by lazy {
        mutableListOf<Movie>().apply {
            repeat(5) {
                add(Movie(  "Lesson 10, how to train?", "description",
                    "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4",
                    ""))

                add(Movie(   "Big bunny duck", "description",
                    "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                    ""))

                add(Movie( "Sample robots video","description",
                    "http://techslides.com/demos/sample-videos/small.mp4",
                    ""))
            }
        }
    }
}
