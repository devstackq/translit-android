package kz.kaspi.translit.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_translate.*
import kz.kaspi.translit.R
import kz.kaspi.translit.ui.adapters.YandexAdapter
import kz.kaspi.translit.models.YandexData
import kz.kaspi.translit.models.YandexDataAdapter
import kz.kaspi.translit.utils.ApiRequests
import kz.kaspi.translit.utils.SharedPreferencesHelper
import kz.kaspi.translit.utils.YandexApi.Companion.KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YandexFragment : Fragment() {

    private val list = mutableListOf<YandexDataAdapter>()
    private val yandexData = YandexDataAdapter()

    lateinit var pref: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    var lang = "kk-ru"
        val apiRequests = ApiRequests.create()
        //config layout manager
        pref =
            SharedPreferencesHelper(activity?.baseContext)


        val yandexLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val yandexAdapter = YandexAdapter()

        translate.setOnClickListener() {
            val mapJson = HashMap<String, String>()
            mapJson["key"] = KEY
            mapJson["text"] = kazText.text.toString()
            mapJson["lang"] = lang
            mapJson["format"] = "plain"
           // input.text = kazText.text.toString()
            apiRequests
                .getTranslate(mapJson)
                .enqueue(object : Callback<YandexData> {
                    @SuppressLint("SetTextI18n")
                    override fun onFailure(call: Call<YandexData>, t: Throwable) {
                       // result.text = "Ошибка: $t"
                    }
                    override fun onResponse(
                        call: Call<YandexData>,
                        response: Response<YandexData>
                    ) {
                        yandexData.result =  response
                            .body()
                            ?.text
                            .toString()
                            .drop(1)
                            .dropLast(1)

//get value, add each value array list, which receive - clickListener, from main fragm
                        pref.getTransYandexShPref().forEach{
                            list.add(it)
                        }
                        list.add(yandexData)
                        recyclerViewYandex.apply {
                            adapter = yandexAdapter
                            layoutManager = yandexLayoutManager
                        }
                        yandexData.input  =  kazText.text.toString()
                        yandexAdapter.setItems(list)
                    }
                })
        }

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
}
//trnsl.1.1.20200526T183646Z.6a1f5538f7fbd1e4.e52a43209fb44b2f31f2d11b5ad308cf02a4bc32
//config retorifit
// create data class
// connect to yandex api, setting TranslateApi
// request to yandex trans
//response set textview
// if change lang, change request yandex api
//save data sh prefe todo