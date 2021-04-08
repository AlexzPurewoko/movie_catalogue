package id.apwdevs.app.core.domain.usecase

import androidx.paging.PagingData
import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteMovieRepository
import id.apwdevs.app.core.domain.repository.FavoriteTvShowRepository
import kotlinx.coroutines.flow.Flow

class FavoriteInteractor constructor(
        private val favMovieRepository: FavoriteMovieRepository,
        private val favTvShowRepository: FavoriteTvShowRepository
): FavoriteUseCase {
    override fun getAllFavoriteMovies(): Flow<PagingData<Movies>> {
        return favMovieRepository.getAllFavorites()
    }

    override fun getAllFavoriteTvShows(): Flow<PagingData<TvShow>> {
        return favTvShowRepository.getAllFavorites()
    }

    override fun getFavoriteMovie(id: Int): Flow<DetailMovie> {
        return favMovieRepository.getFavoriteMovie(id)
    }

    override fun getFavoriteTvShow(id: Int): Flow<DetailTvShow> {
        return favTvShowRepository.getFavoriteTvShow(id)
    }

    override suspend fun checkIsInFavorite(id: Int, isMovie: Boolean) : Boolean{
        return if(isMovie)
            favMovieRepository.checkIsFavorite(id)
        else
            favTvShowRepository.checkIsFavorite(id)
    }

    override suspend fun saveFavoriteMovie(detailMovie: DetailMovie) {
        return favMovieRepository.save(detailMovie)
    }

    override suspend fun saveFavoriteTvShow(detailTvShow: DetailTvShow) {
        return favTvShowRepository.save(detailTvShow)
    }

    override suspend fun unFavorite(id: Int, isMovie: Boolean) {
        return if(isMovie)
            favMovieRepository.unFavorite(id)
        else
            favTvShowRepository.unFavorite(id)
    }
}