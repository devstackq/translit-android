package kz.kaspi.translit.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kz.kaspi.translit.models.TranslateData
import kz.kaspi.translit.models.YandexDataAdapter
import kz.kaspi.translit.models.YandexModel
import java.lang.reflect.Type


class SharedPreferencesHelper(context:Context?)
{
    companion object {
        const val PREF_KEY_FAVORITE = "SHARED_PREFS_FAV"
        const val PREF_KEY = "SHARED_PREFS"
        const val FAV_KEY = "FAVORITE_KEY"
        const val TRANS_KEY = "key"
        const val YAN_KEY = "YANDEX_KEY"
        const val UID_KEY = "UID_KEY"
        const val LOGGED = "Logged"
        const val USER_NAME = "name"
        const val USER_EMAIL = "email"
        const val USER_PHOTO = "photo"
        const val COUNT_WORD = "count_word"
        const val COUNT_TIME = "count_time"

        val MSG_TYPE: Type = object : TypeToken<MutableList<TranslateData>>() {}.type
    }

    private lateinit var pref:SharedPreferences
    private lateinit var prefT:SharedPreferences
    private lateinit var prefY:SharedPreferences
    private lateinit var prefUid:SharedPreferences
    private lateinit var  prefLog:SharedPreferences
    private lateinit var  prefName:SharedPreferences
    private lateinit var  prefEmail:SharedPreferences
    private lateinit var  prefPhoto:SharedPreferences
    private lateinit var  prefCountWord:SharedPreferences
    private lateinit var  prefCountTime:SharedPreferences

    private val mGson: Gson = Gson()

    init {
        if (context != null) {
            pref = context.getSharedPreferences(PREF_KEY_FAVORITE,Context.MODE_PRIVATE)
            prefT = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            prefY = context.getSharedPreferences(YAN_KEY, Context.MODE_PRIVATE)
            prefUid = context.getSharedPreferences(UID_KEY, Context.MODE_PRIVATE)
            prefLog = context.getSharedPreferences(LOGGED, Context.MODE_PRIVATE)
            prefName =context.getSharedPreferences(LOGGED, Context.MODE_PRIVATE)
            prefEmail = context.getSharedPreferences(LOGGED, Context.MODE_PRIVATE)
            prefPhoto = context.getSharedPreferences(LOGGED, Context.MODE_PRIVATE)
            prefCountWord  = context.getSharedPreferences(LOGGED, Context.MODE_PRIVATE)
            prefCountTime = context.getSharedPreferences(LOGGED, Context.MODE_PRIVATE)
        }
    }
    fun addFavoriteToPrefs(item: TranslateData){
        val msgList: MutableList<TranslateData> = getFavoriteFromPrefs()
        msgList.add(item)
        pref.edit().putString(
            FAV_KEY,mGson.toJson(msgList,
                MSG_TYPE
            )).apply()
    }

    fun getFavoriteFromPrefs(): MutableList <TranslateData>{
        return mGson.fromJson(pref.getString(FAV_KEY,""),
            MSG_TYPE
        )?: ArrayList <TranslateData>()
    }

    fun removeFavoritePrefs(item: TranslateData) {
        val msgList: MutableList<TranslateData> = getFavoriteFromPrefs()
        msgList.remove(item)
        pref.edit().putString(
            FAV_KEY,mGson.toJson(msgList,
                MSG_TYPE
            )).apply()
    }


    fun getTransFromPrefs(): MutableList <TranslateData>{
        return mGson.fromJson(prefT.getString(TRANS_KEY,""),
            MSG_TYPE
        )?: ArrayList <TranslateData>()
    }

    fun removeTransPrefs(item: TranslateData) {
        val msgList: MutableList<TranslateData> = getTransFromPrefs()
        msgList.remove(item)
        prefT.edit().putString(
            TRANS_KEY, mGson.toJson(msgList,
                MSG_TYPE
            )).apply()
    }


//    fun saveTransToPrefs(list: MutableList<TranslateData>) {
//        prefT.edit().clear().putString(
//            TRANS_KEY, mGson.toJson(list,
//                MSG_TYPE
//            )).apply()
//    }
//
//    //yandex trans list adapter
//    fun getTransYandexShPref(): MutableList <YandexDataAdapter>{
//        return mGson.fromJson(prefY.getString(YAN_KEY,""),
//            MSG_TYPE
//        )?: ArrayList <YandexDataAdapter>()
//    }
//user prefs
    fun setUid(uid: String) {
     prefUid.edit().putString(UID_KEY , uid).apply()
    }
    fun getUid() : String? {
        return   prefUid.getString(UID_KEY, "")
    }


    fun getLogged() : Boolean? {
        return prefLog.getBoolean(LOGGED, false)
    }

    fun setLogged(b: Boolean) {
        return prefLog.edit().putBoolean(LOGGED, b).apply()
    }

    fun setNamePref(n: String) {
        return prefName.edit().putString(USER_NAME, n).apply()
    }

    fun getNamePref() : String? {
        return prefName.getString(USER_NAME, "name")
    }

    fun setEmailPref(n: String) {
        return prefEmail.edit().putString(USER_EMAIL, n).apply()
    }

    fun getEmailPref() : String? {
        return prefEmail.getString(USER_EMAIL, "email")
    }


    fun setPhotoPref(n: String) {
        return prefPhoto.edit().putString(USER_PHOTO, n).apply()
    }

    fun getPhotoPref() : String? {
        return prefPhoto.getString(USER_PHOTO, "photo")
    }

    fun setCountWord(n: String, uid: String) {
        return prefCountWord.edit().putString(uid, n).apply()
    }

    fun getCountWord() : String? {
        return prefCountWord.getString(getUid(), "0")
    }

    fun setCountTime(n: String, uid: String) {
        return prefCountTime.edit().putString(uid+"time", n).apply()
    }

    fun getCountTime(uid: String) : String? {
        return prefCountTime.getString (uid+"time", "0")
    }
}
