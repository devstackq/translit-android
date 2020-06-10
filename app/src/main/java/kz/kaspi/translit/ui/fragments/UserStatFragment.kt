package kz.kaspi.translit.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.users_stats.*
import kz.kaspi.translit.R
import kz.kaspi.translit.utils.SharedPreferencesHelper

class UserStatFragment : Fragment(R.layout.users_stats) {
    private lateinit var pref: SharedPreferencesHelper
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        pref = SharedPreferencesHelper(activity?.baseContext)
        val currentUser = mAuth.currentUser

        val t = pref.getCountTime(currentUser?.uid.toString())?.toInt()
        val min = t?.div(60)
        countWord.text = " Аударылған сөздер саны: " + pref.getCountWord()
        //countTime.text =  " Қосымшада өтікзген уақыт саны: " +  pref.getCountTime() + "сек"
        countTime.text = " Қосымшада өтікзген уақыт саны $min мин"
    }
}