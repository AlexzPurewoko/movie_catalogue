package id.apwdevs.app.core.domain.usecase

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import kotlinx.coroutines.flow.Flow

interface DetailUseCase {
    fun getDetailMovie(id: Int): Flow<DetailMovie>
    fun getDetailTvShow(id: Int): Flow<DetailTvShow>
}