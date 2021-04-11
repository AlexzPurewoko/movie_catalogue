package id.apwdevs.app.core.domain.usecase

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import id.apwdevs.app.core.utils.DataType
import id.apwdevs.app.core.utils.State
import kotlinx.coroutines.flow.Flow

class FavInteractor constructor(
        private val favMovieRepository: FavoriteRepository<Movies, DetailMovie>,
        private val favTvShowRepository: FavoriteRepository<TvShow, DetailTvShow>
) : FavUseCase {
    override fun getAllFavoriteMovies(): Flow<State<List<Movies>>> {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteTvShows(): Flow<State<List<TvShow>>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteMovie(id: Int): Flow<State<DetailMovie>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteTvShow(id: Int): Flow<State<DetailTvShow>> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIsInFavorite(id: Int, dataType: DataType): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveFavoriteMovie(detailMovie: DetailMovie) {
        TODO("Not yet implemented")
    }

    override suspend fun saveFavoriteTvShow(detailTvShow: DetailTvShow) {
        TODO("Not yet implemented")
    }

    override suspend fun unFavorite(id: Int, dataType: DataType) {
        TODO("Not yet implemented")
    }

}