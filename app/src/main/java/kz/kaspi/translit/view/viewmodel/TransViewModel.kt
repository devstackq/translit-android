//package kz.kaspi.translit.view.viewmodel
//
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import kz.kaspi.translit.data.entity.TranslateEntity
//import kz.kaspi.translit.data.repository.TranslateRepository
//
//class TransViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: TranslateRepository = TranslateRepository(application)
//    private val allMessagesList: LiveData<List<TranslateEntity>> = repository.getAllMessages()
//
//    fun saveMessage(message: TranslateEntity) {
//        repository.saveMessage(message)
//        println(message)
//    }
//
//    fun updateMessage(message: TranslateEntity) {
//        repository.updateMessage(message)
//    }
//
//    fun deleteMessage(message: TranslateEntity) {
//        repository.deleteMessage(message)
//    }
//    fun getAllMessages() : LiveData<List<TranslateEntity>>  {
//        return allMessagesList
//    }
//
//}