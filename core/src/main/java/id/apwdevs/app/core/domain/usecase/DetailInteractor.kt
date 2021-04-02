package id.apwdevs.app.core.domain.usecase

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.repository.IMoviesRepository
import id.apwdevs.app.core.domain.repository.ITvShowRepository
import kotlinx.coroutines.flow.Flow

class DetailInteractor constructor(
    private val movieRepository: IMoviesRepository,
    private val tvShowRepository: ITvShowRepository
): DetailUseCase {
    override fun getDetailMovie(id: Int): Flow<DetailMovie> {
        return movieRepository.getDetailMovie(id)
    }

    override fun getDetailTvShow(id: Int): Flow<DetailTvShow> {
        return tvShowRepository.getDetailTvShow(id)
    }
}