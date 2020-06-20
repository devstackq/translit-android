package kz.kaspi.translit.view.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kz.kaspi.translit.view.adapters.ViewPageAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.profile_fragment.*
import kz.kaspi.translit.R
import kz.kaspi.translit.utils.SharedPreferencesHelper


class ProfileFragments : Fragment(R.layout.profile_fragment) {
    private lateinit var pref: SharedPreferencesHelper
    private val database = Firebase.database
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private var timeInApp: Long = 0
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        pref = SharedPreferencesHelper(activity?.baseContext)

        //timer, signin start, signout stop
        val timer = object : CountDownTimer(86400000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //get last value, FB DB
                timeInApp++
            }
            // update value FB DB timeInApp
            override fun onFinish() {
                cancel();
                return
            }
        }

        if (pref.getLogged()!!) {
            timer.start()
            val currentUser = mAuth.currentUser

            val account =
                GoogleSignIn.getLastSignedInAccount(requireActivity().applicationContext)
            if (account != null) {
                pref.setUid( currentUser?.uid.toString())
                account.displayName?.let { pref.setNamePref(it) }
                account.email?.let{pref.setEmailPref(it) }

                requireActivity().applicationContext?.let { it ->
                    account.photoUrl?.let { pref.setPhotoPref(it.toString()) }
                    Glide.with(it).load(account.photoUrl).into(userAvatar)
                }
                //save values in FB DB, from sh pref
                val ref = database.getReference("user/${pref.getUid()}/name")
                ref.setValue(pref.getNamePref())

                val refEmail = database.getReference("user/${ pref.getUid()}/email")
                //refEmail.setValue(account.email)
                refEmail.setValue(pref.getEmailPref())
                val refPhoto = database.getReference("user/${pref.getUid()}/photo")
                refPhoto.setValue(pref.getPhotoPref())

                val refCountWord  = database.getReference("user/${pref.getUid()}/countWord")
                refCountWord.setValue(pref.getCountWord())

                val refCountTime  = database.getReference("user/${pref.getUid()}/countTime")
                refCountTime.setValue(pref.getCountTime(currentUser?.uid.toString()))

                //get data -> google account -> save shared pref ->  show View
                userName.text = pref.getNamePref()
                userEmail.text = pref.getEmailPref()
                //get photo sh pref, parse string to url
                Glide.with(this).load(Uri.parse(pref.getPhotoPref())).into( userAvatar)
                val t = pref.getCountTime(currentUser?.uid.toString())?.toInt()
                val min = t?.div(60)

//                countWord.text =  " Аударылған сөздер саны: " + pref.getCountWord()
//                //countTime.text =  " Қосымшада өтікзген уақыт саны: " +  pref.getCountTime() + "сек"
//                countTime.text = " Қосымшада өтікзген уақыт саны $min мин"
            }
        }

        //time update, when signout and signin
        signout.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!
            pref.setLogged(false)
            signout.visibility = View.INVISIBLE
            userAvatar.visibility = View.INVISIBLE
            userName.visibility = View.INVISIBLE
            userEmail.visibility = View.INVISIBLE
            //countWord.visibility = View.INVISIBLE
            googleSignInClient.signOut()
            //17
            val shTime = pref.getCountTime(pref.getUid().toString())
            val lastTime = (shTime?.toLong() ?: 0) + timeInApp
            // pref.setCountTime(timeInApp.toString() +  pref.getCountTime() )
            pref.setCountTime((lastTime).toString(), pref.getUid().toString())

            timeInApp = 0
            timer.onFinish()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.signinFrame, (AccountFragment() as Fragment))?.commit()
        }

        setUpViews()
    }

    private fun setUpViews() {
        activity?.let { safeActivity ->
            viewpager.adapter = ViewPageAdapter(safeActivity)
        }
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> "Жеке статистика"
                1 -> "Барлық қолданушылар"
                else -> "empty"
            }
        }.attach()
    }
}

