package kz.kaspi.translit.view.fragments

import io.reactivex.Observable
import kz.kaspi.translit.models.YandexModel
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YandexApiService {

    @GET("translate")
    fun translateText(
        @Query("key") key: String,
        @Query("text") text: String,
        @Query("lang") lang: String,
        @Query("format") format: String
    ): Observable<YandexModel.Result>

    companion object {
        fun create(): YandexApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .build()
            return retrofit.create(YandexApiService::class.java)
        }
    }
}