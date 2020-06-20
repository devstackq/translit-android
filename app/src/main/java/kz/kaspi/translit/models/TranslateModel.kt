package kz.kaspi.translit.models

import android.app.Application
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kz.kaspi.translit.contract.ContractInterface
import kz.kaspi.translit.data.entity.TranslateEntity
import kz.kaspi.translit.data.repository.TranslateRepository

class TranslateModel(application: Application) :  AndroidViewModel(application), ContractInterface.Model  {

    private var message: TranslateEntity? = null
    private val repository: TranslateRepository = TranslateRepository(application)
    private var disposable = CompositeDisposable()
    override fun getAllMsgKirLat(): LiveData<List<TranslateEntity>> {
        return  repository.getAllMessages()
    }

//give arg, main fragment, call presetnter startFunc(arg) -> presenter save value -> model saveMsgKirLat
     override fun saveMessageKirLat(input : String) {

    val translit : Observable<String> = Observable.create { emitter ->
        emitter.onNext(input)
        emitter.onComplete()
    }

    val subscribe: Disposable =
        //get value input,
        translit
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { argument ->
                    //save values Db
                    println(Thread.currentThread() == Looper.getMainLooper().thread)
                    if (argument != null) {
                        val id = if (message != null) message?.id else null
                        val todo = TranslateEntity(
                            id = id,
                            cyrillic = argument,
                            latin = transSentences(argument)
                        )
                        repository.saveMessage(todo)
                        //save item, room-> list entity, adapter call ->  setDiffValue(oldList, newList), compare data, if unique by id, add item
                    }
                },
                {
                    Log.e("app", "Error occurred", it)
                }
            )
    disposable.add(subscribe)
     }

     private fun transSentences(str: String) : String {

         var newWord: String = ""
         var accumLetters: String = ""
         for ((_, letter) in str.withIndex()) {
             if (letter >= 32.toChar() && letter <= 64.toChar()) {
                 newWord += letter.toString()
             } else if (letter > 1000.toChar() && letter < 1300.toChar()) {
                 accumLetters = checkLetter(letter.toString())
                 newWord += accumLetters
             } else if (letter >= 65.toChar() && letter <= 122.toChar()) {
                 newWord = "enter cyrillic symbols.."
             }
         }
         return newWord
     }


     private fun checkLetter(s: String): String {
         var latinLetter: String = ""

         when (s) {
             "а" -> latinLetter = "a"
             "ә" -> latinLetter = "á"
             "б" -> latinLetter = "b"
             "в" -> latinLetter = "v"
             "г" -> latinLetter = "g"
             "д" -> latinLetter = "d"
             "е" -> latinLetter = "e"
             "ж" -> latinLetter = "j"
             "з" -> latinLetter = "z"
             "и" -> latinLetter = "i"
             "й" -> latinLetter = "i"
             "к" -> latinLetter = "k"
             "қ" -> latinLetter = "q"
             "л" -> latinLetter = "l"
             "м" -> latinLetter = "m"
             "н" -> latinLetter = "n"
             "ң" -> latinLetter = "ń"
             "о" -> latinLetter = "o"
             "ө" -> latinLetter = "ó"
             "п" -> latinLetter = "p"
             "р" -> latinLetter = "r"
             "с" -> latinLetter = "s"
             "т" -> latinLetter = "t"
             "у" -> latinLetter = "ý"
             "ұ" -> latinLetter = "u"
             "ү" -> latinLetter = "ú"
             "ф" -> latinLetter = "f"
             "х" -> latinLetter = "h"
             "һ" -> latinLetter = "h"
             "ч" -> latinLetter = "ch"
             "ш" -> latinLetter = "sh"
             "ы" -> latinLetter = "y"
             "і" -> latinLetter = "i"
             "А" -> latinLetter = "A"
             "Ә" -> latinLetter = "Á"
             "Б" -> latinLetter = "B"
             "В" -> latinLetter = "V"
             "Г" -> latinLetter = "G"
             "Ғ" -> latinLetter = "Ǵ"
             "Д" -> latinLetter = "D"
             "Е" -> latinLetter = "Е"
             "Ж" -> latinLetter = "J"
             "З" -> latinLetter = "Z"
             "И" -> latinLetter = "I"
             "Й" -> latinLetter = "I"
             "К" -> latinLetter = "K"
             "Қ" -> latinLetter = "Q"
             "Л" -> latinLetter = "L"
             "М" -> latinLetter = "M"
             "Н" -> latinLetter = "N"
             "Ң" -> latinLetter = "Ń"
             "О" -> latinLetter = "O"
             "Ө" -> latinLetter = "Ó"
             "П" -> latinLetter = "P"
             "Р" -> latinLetter = "R"
             "С" -> latinLetter = "S"
             "Т" -> latinLetter = "T"
             "У" -> latinLetter = "Ý"
             "Ұ" -> latinLetter = "U"
             "Ү" -> latinLetter = "Ú"
             "Ф" -> latinLetter = "F"
             "Х" -> latinLetter = "H"
             "Һ" -> latinLetter = "H"
             "Ч" -> latinLetter = "Ch"
             "Ш" -> latinLetter = "Sh"
             "Ы" -> latinLetter = "Y"
             "І" -> latinLetter = "I"
         }
         return latinLetter
     }
}


