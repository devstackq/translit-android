package kz.kaspi.translit.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kz.kaspi.translit.data.TranslateDatabase
import kz.kaspi.translit.data.dao.TranslateDao
import kz.kaspi.translit.data.entity.TranslateEntity

class TranslateRepository(application: Application) {
 // 1 entity, 2 dao-interface query DB,  3 repos -> call method, to DB,  4 viewmodel, 5 call from activity, adapter
    private val translateDao : TranslateDao
    private val allMessages : LiveData<List<TranslateEntity>>

    init {
        val db = TranslateDatabase.getInstance(application.applicationContext)
        translateDao = db!!.translateDao()
        allMessages = translateDao.getAllMessages()
    }

    fun saveMessage(message: TranslateEntity) = runBlocking {
        this.launch (Dispatchers.IO) {
            translateDao.saveMessage(message)
        }
    }
    fun updateMessage(message: TranslateEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            translateDao.updateMessage(message)
        }
    }
    fun deleteMessage(message: TranslateEntity) = runBlocking {
        this.launch(Dispatchers.IO) {
            translateDao.deleteMessage(message)
        }
    }

    fun getAllMessages() : LiveData<List<TranslateEntity>>  {
        return allMessages
    }

}