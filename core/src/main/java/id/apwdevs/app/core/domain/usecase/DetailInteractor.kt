package id.apwdevs.app.core.domain.usecase

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.repository.MovieRepository
import id.apwdevs.app.core.domain.repository.TvShowRepository
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

class DetailInteractor constructor(
        private val movieRepository: MovieRepository,
        private val tvShowRepository: TvShowRepository
): DetailUseCase {
    override fun getDetailMovie(id: Int): Flow<State<DetailMovie>> {
        return movieRepository.getDetailMovie(id)
    }

    override fun getDetailTvShow(id: Int): Flow<State<DetailTvShow>> {
        return tvShowRepository.getDetailTvShow(id)
    }
}