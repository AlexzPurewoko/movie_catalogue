package id.apwdevs.app.core.domain.usecase

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavMovieRepository
import id.apwdevs.app.core.domain.repository.FavTvShowRepository
import id.apwdevs.app.core.utils.DataType
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

class FavInteractor constructor(
    private val favMovieRepository: FavMovieRepository,
    private val favTvShowRepository: FavTvShowRepository
) : FavUseCase {
    override fun getAllFavoriteMovies(): Flow<State<List<Movies>>> {
        return favMovieRepository.getAllFavorites()
    }

    override fun getAllFavoriteTvShows(): Flow<State<List<TvShow>>> {
        return favTvShowRepository.getAllFavorites()
    }

    override fun getFavoriteMovie(id: Int): Flow<State<DetailMovie>> {
        return favMovieRepository.getFavoriteDetailItem(id)
    }

    override fun getFavoriteTvShow(id: Int): Flow<State<DetailTvShow>> {
        return favTvShowRepository.getFavoriteDetailItem(id)
    }

    override suspend fun checkIsInFavorite(id: Int, dataType: DataType): Boolean {
        return when (dataType) {
            DataType.MOVIES -> favMovieRepository.checkIsFavorite(id)
            DataType.TVSHOW -> favTvShowRepository.checkIsFavorite(id)
        }
    }

    override suspend fun saveFavoriteMovie(detailMovie: DetailMovie) {
        return favMovieRepository.save(detailMovie)
    }

    override suspend fun saveFavoriteTvShow(detailTvShow: DetailTvShow) {
        return favTvShowRepository.save(detailTvShow)
    }

    override suspend fun unFavorite(id: Int, dataType: DataType) {
        when (dataType) {
            DataType.MOVIES -> favMovieRepository.unFavorite(id)
            DataType.TVSHOW -> favTvShowRepository.unFavorite(id)
        }
    }

}