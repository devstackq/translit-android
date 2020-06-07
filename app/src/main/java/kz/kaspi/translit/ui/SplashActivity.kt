package kz.kaspi.translit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.splash_activity.*
import kz.kaspi.translit.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        splashImage.animate().setDuration(1000).alpha(1f).withEndAction {
                val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
           // splashImage.setVisibility(View.GONE);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}