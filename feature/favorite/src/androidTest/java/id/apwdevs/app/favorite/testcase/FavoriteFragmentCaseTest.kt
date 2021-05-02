package id.apwdevs.app.favorite.testcase

import com.google.gson.Gson
import id.apwdevs.app.core.utils.DomainToEntityMapper
import id.apwdevs.app.core.utils.RemoteToDomainMapper
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteMovieSource
import id.apwdevs.app.data.source.local.database.favlocal.FavoriteTvShowSource
import id.apwdevs.app.data.source.local.database.paging.PagingCaseMovieDb
import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.favorite.di.favoriteModule
import id.apwdevs.app.libs.util.AssetDataJson
import id.apwdevs.app.libs.util.readJson
import id.apwdevs.app.res.util.PageType
import id.apwdevs.app.test.androdtest.BaseAndroidTest
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject

abstract class FavoriteFragmentCaseTest : BaseAndroidTest() {


    private val appDatabase by inject(AppDatabase::class.java)

    override fun setup() {
        super.setup()
        loadKoinModules(favoriteModule)

    }

    override fun tearDown() {
        super.tearDown()
        unloadKoinModules(favoriteModule)
        appDatabase.clearAllTables()
    }

    @Test
    abstract fun should_display_no_data_if_not_have_data_in_database()

    @Test
    abstract fun should_display_data_if_have_any_data_in_database()

    @Test
    abstract fun should_display_no_data_whereby_any_data_but_not_to_display_in_this_page()

    @Test
    abstract fun can_move_to_next_page_when_clicking_next_button()

    protected suspend fun prepopulateDataInMemoryDatabase(pageType: PageType): Any {
        val favCaseDb = get(PagingCaseMovieDb::class.java)
        val detailCaseMovieDb = get(FavoriteMovieSource::class.java)
        val detailCaseTvShowDb = get(FavoriteTvShowSource::class.java)
        val gson = Gson()

        return when(pageType){
            PageType.MOVIES -> {
                val genreMovies = context.readJson(AssetDataJson.GENRE_MOVIES)
                val detailMovie = context.readJson(AssetDataJson.DETAIL_MOVIE_OK)
                val jsonGenreMovies = gson.fromJson(genreMovies, GenreResponse::class.java)
                val detailMovieObj = gson.fromJson(detailMovie, MovieDetailResponse::class.java)

                favCaseDb.insertGenres(
                    DomainToEntityMapper.domainGenreToEntityRoom(
                        RemoteToDomainMapper.genres(jsonGenreMovies.genres
                        ))
                )

                detailCaseMovieDb.save(
                    DomainToEntityMapper.favDetailMovie(
                        RemoteToDomainMapper.detailMovie(detailMovieObj)
                    )
                )
                detailMovieObj
            }
            PageType.TV_SHOW -> {
                val genreTvShows = context.readJson(AssetDataJson.GENRE_TV)
                val detailTvShow = context.readJson(AssetDataJson.DETAIL_TVSHOW_OK)

                // convert to Object
                val jsonGenreTvShow = gson.fromJson(genreTvShows, GenreResponse::class.java)
                val detailTvShowObj = gson.fromJson(detailTvShow, TvDetailResponse::class.java)

                favCaseDb.insertGenres(
                    DomainToEntityMapper.domainGenreToEntityRoom(
                        RemoteToDomainMapper.genres(jsonGenreTvShow.genres
                        ))
                )

                detailCaseTvShowDb.save(
                    DomainToEntityMapper.favDetailTvShow(
                        RemoteToDomainMapper.detailTvShow(detailTvShowObj)
                    )
                )
                detailTvShowObj
            }
        }


    }

}


