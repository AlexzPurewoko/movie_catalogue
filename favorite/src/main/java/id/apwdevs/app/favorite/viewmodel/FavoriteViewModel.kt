package id.apwdevs.app.favorite.viewmodel

import androidx.lifecycle.ViewModel
import id.apwdevs.app.core.domain.usecase.FavoriteUseCase

class FavoriteViewModel(
    private val favoriteUseCase: FavoriteUseCase
): ViewModel() {

}