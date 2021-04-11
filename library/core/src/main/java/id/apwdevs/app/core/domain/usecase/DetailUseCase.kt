package id.apwdevs.app.core.domain.usecase

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

interface DetailUseCase {
    fun getDetailMovie(id: Int): Flow<State<DetailMovie>>
    fun getDetailTvShow(id: Int): Flow<State<DetailTvShow>>
}