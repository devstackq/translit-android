package kz.kaspi.translit.utils

import kz.kaspi.translit.models.YandexData
import kz.kaspi.translit.utils.YandexApi.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiRequests {
    companion object Factory {
        fun create(): ApiRequests {
            val client = OkHttpClient().newBuilder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()
            return retrofit.create(ApiRequests::class.java)
        }
        }

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    fun getTranslate(@FieldMap map: Map<String,String>): Call<YandexData>
}