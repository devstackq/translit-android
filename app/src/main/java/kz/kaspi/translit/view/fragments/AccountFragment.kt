package kz.kaspi.translit.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.signin_fragment.*
import kz.kaspi.translit.R
import kz.kaspi.translit.utils.SharedPreferencesHelper

class AccountFragment : Fragment(R.layout.signin_fragment) {

    companion object {
        private const val GOOOGLE_CODE = 777
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var pref: SharedPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pref = SharedPreferencesHelper(activity?.baseContext)
        // Config google signin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // get client by google signin options
        googleSignInClient = requireActivity().let { GoogleSignIn.getClient(it, gso) }!!
// if not logged, signin, else -> redirect profile fragment
        if (!pref.getLogged()!!) {
            signin_btn.setOnClickListener {
                signIn()
            }
        } else if (pref.getLogged()!!) {
            signinFrame.removeAllViews()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.signinFrame, (ProfileFragment() as Fragment)).commit()
        }
    }

    private fun signIn() {
        // get request to google, and get data onActivityResult
        val signInClient = googleSignInClient.signInIntent
        startActivityForResult(signInClient, GOOOGLE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOOGLE_CODE) {
            // give data, google user data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInFragment", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInFragment", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInFragment", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        pref.setLogged(true)
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SignInFragment", "signInWithCredential:success")
                        // send value profile fragment, and hide signin views
                        signinFrame.removeAllViews()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.signinFrame, (ProfileFragment() as Fragment)).commit()
                    } else {
                        Log.d("SignInFragment", "signInWithCredential:failure")
                    }
                }
        } else {
            Log.d("SignInFragment", "Signin failed")
        }
    }
}