package kz.kaspi.translit.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kz.kaspi.translit.view.fragments.TotalStatsFragment
import kz.kaspi.translit.view.fragments.UserStatFragment
import java.lang.NullPointerException

class ViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val NUM_PAGES = 2
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserStatFragment()
            1 -> TotalStatsFragment()
            else -> throw NullPointerException("Invalid position has been requested")
        }
    }
}