package kz.kaspi.translit.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorites.*
import kz.kaspi.translit.R
import kz.kaspi.translit.utils.SharedPreferencesHelper
import kz.kaspi.translit.models.TranslateData
import kz.kaspi.translit.adapters.FavoriteAdapter


class FavoritesFragment : Fragment() {

    private var favorites = mutableListOf<TranslateData>()
    lateinit var pref: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref =
            SharedPreferencesHelper(activity?.baseContext)
//get value, add each value array list, which receive - clickListener, from main fragm
            pref.getFavoriteFromPrefs().forEach{
            favorites.add(it)
        }

        val favLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val favoriteAdapter = FavoriteAdapter(
            pref::removeFavoritePrefs,
            pref::addFavoriteToPrefs
        )

        listFavorites.apply {
            adapter = favoriteAdapter
            layoutManager = favLayoutManager
        }
        favoriteAdapter.setItems(favorites)
    }
}