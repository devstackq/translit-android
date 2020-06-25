package kz.kaspi.translit.view.fragments

import android.content.Context
import android.os.Bundle
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
import kz.kaspi.translit.R
import kz.kaspi.translit.contract.ContractInterface
import kz.kaspi.translit.utils.ItemDecoration
import kz.kaspi.translit.view.adapters.MainAdapter
import kotlinx.android.synthetic.main.fragment_main.listTranslateView
import kotlinx.android.synthetic.main.fragment_main.send_btn
import kotlinx.android.synthetic.main.fragment_main.toolbar
import kz.kaspi.translit.data.entity.TranslateEntity
import kz.kaspi.translit.utils.SharedPreferencesHelper
import kz.kaspi.translit.view.viewmodel.TransViewModel

class MainFragment : Fragment() {

    private val messages = mutableListOf<TranslateEntity>()
    companion object {
        lateinit   var viewmodel: TransViewModel
        lateinit var pref: SharedPreferencesHelper
        lateinit var transLayoutManager: LinearLayoutManager
        lateinit var context: Context
    }

    private lateinit var transAdapterRoom: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pref = SharedPreferencesHelper(activity?.baseContext)
//        viewmodel = ViewModelProviders.of(this).get(TranslateModel::class.java)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //presenter = TranslatePresenter(this)
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProviders.of(this).get(TransViewModel::class.java)
        updateViewData()
// get value theme, change night mode/true/false -> change night mode
        val themePref = activity?.getSharedPreferences("ThemeMode", Context.MODE_PRIVATE)
        val shPref = themePref?.edit()
        val isNightMode: Boolean? = themePref?.getBoolean("theme", false)

        if (isNightMode!!) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

// theme change
        val btnTheme = view.findViewById<Button>(R.id.changeTheme)!!
        btnTheme.setOnClickListener {
            if (isNightMode) {
                // if night -> off night mode, chage theme value(sh pref) -> false,
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                shPref?.putBoolean("theme", false)
                shPref?.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                shPref?.putBoolean("theme", true)
                shPref?.apply()
            }
        }

        // context menu register / scrollbar
        val mRecyclerView = view.findViewById<RecyclerView>(R.id.listTranslateView)
        registerForContextMenu(mRecyclerView)

// todo
        // add favorites - click context menu/remove, -> update page
        // val transAdapter = MainAdapter(
//            pref::addFavoriteToPrefs,
//            pref::removeTransPrefs
        // )

        val itemDecoration =
            ItemDecoration(10, 12)
        listTranslateView.addItemDecoration(itemDecoration)

        // toolbar option menu like popup menu
        toolbar.inflateMenu(R.menu.popup_menu)

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
        transLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        listTranslateView.layoutManager = transLayoutManager
        transAdapterRoom = activity?.let {
            MainAdapter(
                it
                //    pref::addFavoriteToPrefs
                //  pref::removeTransPrefs
            )
        }!!
        // when create post -> go to bottom, last created post
        transAdapterRoom = activity?.let { MainAdapter(it) }!!
        listTranslateView.adapter = transAdapterRoom

        send_btn.setOnClickListener {
            val input = view.findViewById<EditText>(R.id.inputtext)!!
            viewmodel.saveMessage(input.text.toString())

            input.text = null
            val scrollerSmooth = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int =
                    SNAP_TO_START
            }
            scrollerSmooth.targetPosition = messages.size
            //transLayoutManager.startSmoothScroll(scrollerSmooth)
            updateViewData()
        }
    }

    private fun updateViewData() {
        viewmodel.getAllMessages()?.observe(viewLifecycleOwner, Observer {
            it.let { it1 -> transAdapterRoom.setDiffValue(it1) }
            })
    }
}