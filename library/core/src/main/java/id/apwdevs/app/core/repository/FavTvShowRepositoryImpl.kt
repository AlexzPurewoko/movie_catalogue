package id.apwdevs.app.core.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Genre
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteTvShowRepository
import id.apwdevs.app.core.utils.DomainToEntityMapper
import id.apwdevs.app.core.utils.EntityToDomainMapper
import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.paging.TvShowPagingSource
import id.apwdevs.app.data.source.local.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Deprecated("will be deleted!")
class FavTvShowRepositoryImpl constructor(
        private val accessDb: AppDatabase
) : FavoriteTvShowRepository {

    private val genres = mutableListOf<Genres>()
    override fun getAllFavorites(): Flow<PagingData<TvShow>> {
        return Pager(
                config = PagingConfig(TvShowPagingSource.LOAD_SIZE, enablePlaceholders = false),
                pagingSourceFactory = { TvShowPagingSource(accessDb) }
        ).flow.map { pagingData ->
            getGenre()
            pagingData.map {
                val listGenres = accessDb.genreDao().genreIdsMapper(it.id)
                val genres = listGenres.map{
                    val item = genres.find { i -> i.id == it }
                    if(item == null) Genre(0, "")
                    else Genre(item.id, item.genreName)
                }
                TvShow(
                        tvId = it.id, name = it.name, firstAirDate = it.firstAirDate, overview = it.overview,
                        language = it.originalLanguage, genres = genres, posterPath = it.posterPath,
                        backdropPath = it.backdropPath, voteAverage = it.voteAverage, voteCount = it.voteCount
                )
            }
        }
    }

    override fun getFavoriteTvShow(id: Int): Flow<DetailTvShow> {
        return flow {
            val data = accessDb.favTvShowDetail().getTvShow(id)
            val detailTvShow = EntityToDomainMapper.detailTvShow(data)
            emit(detailTvShow)
        }
    }

    override suspend fun checkIsFavorite(id: Int): Boolean {
        return accessDb.favTvShowDetail().isTvShowExists(id)
    }

    override suspend fun save(detailTvShow: DetailTvShow) {
        val favEntity = DomainToEntityMapper.favDetailTvShow(detailTvShow)
        accessDb.favTvShowDetail().insertFavDetailTvShow(favEntity)
    }

    override suspend fun unFavorite(id: Int) {
        accessDb.favTvShowDetail().deleteById(id)
    }

    private suspend fun getGenre() {
        if(genres.isNotEmpty()) return
        val genresFromDao = accessDb.genreDao().getAllGenres()
        genres.addAll(genresFromDao)
    }
}

