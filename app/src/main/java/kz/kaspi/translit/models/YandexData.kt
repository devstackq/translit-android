package kz.kaspi.translit.models

object YandexModel{
    data class Result(val code: Int,
                      val lang: String,
                      val text: ArrayList<String>)
}
