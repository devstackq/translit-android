package kz.kaspi.translit.ui.fragments


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import kz.kaspi.translit.R
import kz.kaspi.translit.adapters.MainAdapter
import kz.kaspi.translit.data.entity.TranslateEntity
import kz.kaspi.translit.models.TranslateData
import kz.kaspi.translit.models.User
import kz.kaspi.translit.ui.viewmodel.TransViewModel
import kz.kaspi.translit.utils.SharedPreferencesHelper
import kz.kaspi.translit.utils.StudentItemDecoration

class MainFragments :  Fragment() {

    private val data = mutableListOf<TranslateData>()
    //    private val translated = TranslateData()
    private lateinit var pref: SharedPreferencesHelper
    private var disposable = CompositeDisposable()
    private val database = Firebase.database
    private  var firebaseAuth  = FirebaseAuth.getInstance()

    //room
    // private var messageListAd : List<TranslateEntity> = arrayListOf()
    private lateinit var transAdapterRoom : MainAdapter
    var message: TranslateEntity? = null
    private lateinit var msgViewModel: TransViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreferencesHelper(activity?.baseContext)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          val  arg = view.findViewById<EditText>(R.id.inputtext)!!
         val translit : Observable<String> = Observable.create { emitter ->
             emitter.onNext(arg.text.toString())
             emitter.onComplete()
         }

        val transLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        listTranslateView.layoutManager = transLayoutManager
        transAdapterRoom = activity?.let {
            MainAdapter(
                it
              //    pref::addFavoriteToPrefs
                //  pref::removeTransPrefs
            )
        }!!

        transAdapterRoom = activity?.let { MainAdapter(it) }!!
        listTranslateView.adapter = transAdapterRoom

        msgViewModel = ViewModelProviders.of(this).get(TransViewModel::class.java)
        msgViewModel.getAllMessages().observe(viewLifecycleOwner, Observer {
            it?.let { it1 -> transAdapterRoom.setAllItems(it1) }
        })


//get value theme, change night mode/true/false -> change night mode
        val themePref = activity?.getSharedPreferences("ThemeMode", Context.MODE_PRIVATE)
        val  shPref  = themePref?.edit()
        val isNightMode : Boolean? = themePref?.getBoolean("theme", false)

        if(isNightMode!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

//theme change
        val  btnTheme = view.findViewById<Button>(R.id.themeButton)!!
        btnTheme.setOnClickListener {
            if (isNightMode) {
                //if night -> off night mode, chage theme value(sh pref) -> false,
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                shPref?.putBoolean("theme", false)
                shPref?.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                shPref?.putBoolean("theme", true)
                shPref?.apply()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        //context menu register
        val mRecyclerView = view.findViewById<RecyclerView>(R.id.listTranslateView)
        registerForContextMenu(mRecyclerView)

//todo
        //add favorites - click context menu/remove, -> update page
      // val transAdapter = MainAdapter(
//            pref::addFavoriteToPrefs,
//            pref::removeTransPrefs
        //)

        val itemDecoration =
            StudentItemDecoration(10, 12)
        listTranslateView.addItemDecoration(itemDecoration)

        //scroll bottom, when click btn
        scroll.ellipsize = TextUtils.TruncateAt.MARQUEE
        scroll.isSingleLine = true
        scroll.isSelected = true

       scroll.setOnClickListener { listTranslateView.smoothScrollToPosition(data.size) }
        scroll.text = ""
    //config scroll botoom visible/gone
        // not work - bacause -> change save data, before : shared pref, then room
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0) {
                    if (scroll.isShown) {
                        scroll.visibility = View.GONE
                    }
                }
                if(dy > 0 ) {
                    if (!scroll.isShown) {
                        scroll.visibility = View.VISIBLE
                    }
            }
        }
        })

//when event -> //fill value, data classetting layout manager, then setItems, values data class
        send_btn.setOnClickListener {
            if (arg.text.toString() != "") {
                //start operation, other thread, then return values main thread
                val subscribe: Disposable =
                    //get value input,
                    translit
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { argument ->
                                //save values Db
                                if (argument != null) {
                                    val id = if (message != null) message?.id else null
                                    val todo = TranslateEntity(
                                        id = id,
                                        cyrillic = argument,
                                        latin = transSentences(argument)
                                    )
                                    msgViewModel.saveMessage(todo)
                                }
                            },
                            {
                                Log.e("app", "Error occurred", it)
                            }
                        )
                disposable.add(subscribe)

//fill, each TransData fileds -> values, and add array TransData - to sh prefs

                arg.text = null
                //when create post -> go to bottom, last created post
                val scrollerSmooth = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int =
                        SNAP_TO_START
                }
                scrollerSmooth.targetPosition = data.size - 1
//                transLayoutManager.startSmoothScroll(scrollerSmooth)
            }
        }

        //toolbar option menu like popup menu
        toolbar.inflateMenu(R.menu.popup_menu);

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sendLetter -> {
                    Toast.makeText(
                        context,
                        "You Clicked : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                R.id.share -> {
                    Toast.makeText(
                        context,
                        "You Clicked : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        context,
                        "You Clicked : " + item.title,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            false
        }
    }

    //func translate
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

        //count split, regex, last count, get sh pref, sum, countCurrent + countShPref, update count Shpref
     val countWord = newWord.split("\\s+".toRegex()).size.toLong()
        val last = pref.getCountWord()
       val lastSumCurrent =  last?.toLong()?.plus(countWord)
        pref.setCountWord(lastSumCurrent.toString(), pref.getUid().toString())
        println(last)
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
//tablyoyt, 2 fragments (viewpager)
//viewpagerFragemnt, left user statistics, -> adpter userStat(value sh pref)
//right side - adapter - static data, list 10 user, - adapter generalStaticAdapater