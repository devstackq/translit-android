package kz.kaspi.translit.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.user_total.*
import kz.kaspi.translit.R
import kz.kaspi.translit.models.Statistics
import kz.kaspi.translit.view.adapters.UserStatsListAdapter


class TotalStatsFragment : Fragment() {

    private var users = mutableListOf<Statistics>()
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_total, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences("usersStats", Context.MODE_PRIVATE)

        try {
            val prefValue: String? = sharedPreferences?.getString("user", "")
            val gsonToken = object : TypeToken<List<Statistics>>() {}.type
            val obj = Gson().fromJson<List<Statistics>>(prefValue, gsonToken)

            users.clear()
            users.addAll(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val statsAdapter = UserStatsListAdapter()
        val statsLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        //insert view xml, data
        userStats.apply {
            adapter = statsAdapter
            layoutManager = statsLayoutManager
        }
        statsAdapter.setItems(users)

    }
}