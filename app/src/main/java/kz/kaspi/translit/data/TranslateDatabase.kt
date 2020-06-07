package kz.kaspi.translit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.kaspi.translit.data.dao.TranslateDao
import kz.kaspi.translit.data.entity.TranslateEntity

@Database(entities = [TranslateEntity::class], version = 1, exportSchema = false)
abstract class TranslateDatabase : RoomDatabase() {

    abstract fun translateDao(): TranslateDao
//if not create db, -> create, else use this db
    companion object {
    private var INSTANCE : TranslateDatabase? = null

    fun getInstance(context: Context) : TranslateDatabase? {
        if(INSTANCE == null) {
            synchronized(TranslateDatabase::class) {
                INSTANCE = Room.databaseBuilder(context,
                TranslateDatabase::class.java,
                "trans_db")
                    .build()
            }
        }
        return INSTANCE
    }
}
}