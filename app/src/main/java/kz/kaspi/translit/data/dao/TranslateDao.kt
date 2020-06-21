package kz.kaspi.translit.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kz.kaspi.translit.data.entity.TranslateEntity

@Dao
interface TranslateDao {
    @Insert
    fun saveMessage(message: TranslateEntity)

    @Delete
    fun deleteMessage(message: TranslateEntity)

    @Update
    fun updateMessage(message: TranslateEntity)

    @Query("SELECT * FROM translate_table ORDER BY id ASC")
    fun getAllMessages(): LiveData<List<TranslateEntity>>
}