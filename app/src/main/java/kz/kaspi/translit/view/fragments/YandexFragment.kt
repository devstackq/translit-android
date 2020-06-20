package kz.kaspi.translit.view.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_translate.*
import kz.kaspi.translit.R
import kz.kaspi.translit.utils.SharedPreferencesHelper


//rxjava, change lang -> toolbar
class YandexFragment : Fragment() {

    companion object {
        private val API_KEY: String =
            "trnsl.1.1.20170330T085156Z.928b6e6d5afb8d9a.32082885800f6b054b0b0ec2becc4adf884fb27a"
    }
    private val yandexApiService by lazy {
        YandexApiService.create()
    }
    lateinit var pref: SharedPreferencesHelper
    private var disposable: Disposable? = null
    private var lang : String? = "kk-en"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initiateViews()

        toolbarTranslate.inflateMenu(R.menu.choice_lang);

        toolbarTranslate.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.kzrus -> {
                    lang = "kk-ru"
                    Toast.makeText(
                        context,
                        "You choice : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.kzeng -> {
                    lang = "kk-en"
                    Toast.makeText(
                        context,
                        "You choice : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            false
        }
    }

    private fun initiateViews() {
        translate.setOnClickListener {
            beginSearch( kazText.text.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun beginSearch(arg: String) {
        input_lng.text = arg
        disposable = lang?.let {
            yandexApiService
                .translateText(API_KEY, arg, it, "plain")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result -> translated_text.text = result.text[0] }
        }
        pref =
            SharedPreferencesHelper(activity?.baseContext)
    }
}
