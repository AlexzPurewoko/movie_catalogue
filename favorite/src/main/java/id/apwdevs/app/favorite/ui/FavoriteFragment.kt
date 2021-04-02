package id.apwdevs.app.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.apwdevs.app.favorite.databinding.FragmentFavoriteBinding
import id.apwdevs.app.favorite.di.favoriteModule
import id.apwdevs.app.res.BaseFeatureFragment
import org.koin.core.module.Module

class FavoriteFragment : BaseFeatureFragment() {

    private lateinit var binding: FragmentFavoriteBinding

    override val koinModules: List<Module>
        get() = listOf(favoriteModule)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


}