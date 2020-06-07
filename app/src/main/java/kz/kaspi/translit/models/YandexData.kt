package kz.kaspi.translit.models

data class YandexData(private val code: Int, private val lang: String, var input: String, var text: ArrayList<String>)