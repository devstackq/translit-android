package kz.kaspi.translit.ui

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.videoplayer_view.view.*
import kz.kaspi.translit.R


class VideoPlayerView
@JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var isSeekBarVisible = false
    private lateinit var runnable: Runnable
    private val handlerView: Handler? = Handler()
    private var isPlayBtnVisible = true
    private var isPauseBtnVisible = false

    init {
        inflate(context, R.layout.videoplayer_view, this)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.VideoPlayerView,
                defStyleAttr,
                0
            )
            isSeekBarVisible = typedArray.getBoolean(
                R.styleable.VideoPlayerView_isSeekBarVisible,
                isSeekBarVisible
            )
            seekBar.isVisible = isSeekBarVisible
            playVideo.isVisible = isPlayBtnVisible
            pauseVideo.isVisible = isPauseBtnVisible
            typedArray.recycle()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBar?.progress = videoView.currentPosition
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                videoView.seekTo(seekBar?.progress ?: 0)
                videoView.start()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoView.seekTo(seekBar?.progress ?: 0)
                videoView.start()
            }
        })
    }
//todo after
    fun pause() {
        videoView.pause()
         pauseVideo.isVisible = false
         isPauseBtnVisible = true
         isPlayBtnVisible = false
    }

    fun play(url: String) {
        val uri: Uri = Uri.parse(url)
        videoView.setVideoURI(uri)
        videoView.start()
        playVideo.isVisible = false
        videoView.setOnPreparedListener {
            seekBar.max = videoView.duration
            listenPlayerTrack()
        }
    }

    private fun listenPlayerTrack(){
        runnable = Runnable {
            seekBar.progress = videoView.currentPosition
            handlerView?.postDelayed(runnable, 100)
        }
        handlerView?.postDelayed(runnable, 100)
    }
}
