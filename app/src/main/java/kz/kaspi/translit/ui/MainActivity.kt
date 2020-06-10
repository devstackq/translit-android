package kz.kaspi.translit.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kz.kaspi.translit.R
import kz.kaspi.translit.fragments.*
import kz.kaspi.translit.ui.fragments.*


open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
           setSupportActionBar(findViewById(R.id.toolbar))
        setContentView(R.layout.activity_main)

        val pref = getSharedPreferences("ThemeMode", Context.MODE_PRIVATE)
        val isModeTheme = pref.getBoolean("theme", false)

        if(isModeTheme) {
            setTheme(R.style.LightTheme)
        }else {
            setTheme(R.style.DarkTheme)
        }

        val navigationView: BottomNavigationView = findViewById(R.id.nav_items)
//        navigationView.menu.findItem(this.nav_items1.id)?.isChecked = true isSelected()
        navigationView.menu.getItem(2)?.isChecked = true

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //set main page, first page render
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.frameLayout,
            MainFragments()
        )
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
//         item.setChecked(true)
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_latins -> selectedFragment =
                    MainFragments()
                R.id.nav_translate -> selectedFragment =
                    YandexFragment()
                R.id.nav_search -> selectedFragment =
                    VideoFragment()
                R.id.nav_account -> selectedFragment =
                    AccountFragment()
                R.id.nav_likes -> selectedFragment =
                    FavoritesFragment()
            }

            val transaction: FragmentTransaction =
                supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, selectedFragment!!)
            transaction.addToBackStack(null)
            transaction.commit()
            true
        }
}