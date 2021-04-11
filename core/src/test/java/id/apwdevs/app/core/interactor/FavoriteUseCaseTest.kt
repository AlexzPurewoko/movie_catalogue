package id.apwdevs.app.core.interactor

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test


class FavoriteUseCaseTest {

    @MockK
    lateinit var favMovieRepository: FavoriteRepository<Movies, DetailMovie>

    @MockK
    lateinit var favTvShowRepository: FavoriteRepository<TvShow, DetailTvShow>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

    }

    @Test
    fun `getAllFavoriteMovies() must call repository and return right value`() {

    }

    @Test
    fun `getAllFavoriteTvShows() must call repository and return right value`() {

    }

    @Test
    fun `getFavoriteMovie() must call repository and return right value`() {

    }

    @Test
    fun `getFavoriteTvShow() must call repository and return right value`() {

    }

    @Test
    fun `checkIsInFavorite() must call to movie favorite repository and return right value`() {

    }

    @Test
    fun `checkIsInFavorite() must call to tvshow favorite repository and return right value`() {

    }

    @Test
    fun `saveFavoriteMovie() must call repository and return right value`() {

    }

    @Test
    fun `saveFavoriteTvShow() must call repository and return right value`() {

    }

    @Test
    fun `unFavorite() must call to movie favorite repository and return right value`() {

    }

    @Test
    fun `unFavorite() must call to tvshow favorite repository and return right value`() {

    }
}