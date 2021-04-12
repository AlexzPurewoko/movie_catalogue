package id.apwdevs.app.core.interactor

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import id.apwdevs.app.core.domain.usecase.FavInteractor
import id.apwdevs.app.core.domain.usecase.FavUseCase
import id.apwdevs.app.core.utils.DataType
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.libs.data.FakeDomain
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class FavoriteUseCaseTest {

    @MockK
    lateinit var favMovieRepository: FavoriteRepository<Movies, DetailMovie>

    @MockK
    lateinit var favTvShowRepository: FavoriteRepository<TvShow, DetailTvShow>

    private lateinit var favoriteUseCase: FavUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        favoriteUseCase = FavInteractor(favMovieRepository, favTvShowRepository)
    }

    @Test
    fun `getAllFavoriteMovies() must call repository and return right value`() {
        runBlocking {
            val movieId = 1
            val fakeData = listOf(FakeDomain.generateMovieDomain(movieId))

            val expected = flow {
                emit(State.Success(fakeData))
            }

            every { favMovieRepository.getAllFavorites() } returns expected

            val actual = favoriteUseCase.getAllFavoriteMovies()

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { favMovieRepository.getAllFavorites() }
            confirmVerified(favMovieRepository)
        }
    }

    @Test
    fun `getAllFavoriteTvShows() must call repository and return right value`() {
        runBlocking {
            val movieId = 1
            val fakeData = listOf(FakeDomain.generateTvShowDomain(movieId))

            val expected = flow {
                emit(State.Success(fakeData))
            }

            every { favTvShowRepository.getAllFavorites() } returns expected

            val actual = favoriteUseCase.getAllFavoriteTvShows()

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { favTvShowRepository.getAllFavorites() }
            confirmVerified(favTvShowRepository)
        }
    }

    @Test
    fun `getFavoriteMovie() must call repository and return right value`() {
        runBlocking {
            val movieId = 1
            val fakeData = listOf(FakeDomain.generateMovieDomain(movieId))

            val expected = flow {
                emit(State.Success(fakeData))
            }

            every { favMovieRepository.getAllFavorites() } returns expected

            val actual = favoriteUseCase.getAllFavoriteMovies()

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { favMovieRepository.getAllFavorites() }
            confirmVerified(favMovieRepository)
        }
    }

    @Test
    fun `getFavoriteTvShow() must call repository and return right value`() {
        runBlocking {
            val movieId = 1
            val fakeData = FakeDomain.generateDetailTvDomain(movieId)

            val expected = flow {
                emit(State.Success(fakeData))
            }

            every { favTvShowRepository.getFavoriteDetailItem(movieId) } returns expected

            val actual = favoriteUseCase.getFavoriteTvShow(movieId)

            Assert.assertNotNull(actual)
            Assert.assertEquals(expected, actual)

            verify(exactly = 1) { favTvShowRepository.getFavoriteDetailItem(movieId) }
            confirmVerified(favMovieRepository)
        }
    }

    @Test
    fun `checkIsInFavorite(movie) must call to movie favorite repository and return right value`() {
        runBlocking {
            val movieId = 1
            val rightValue = true

            coEvery { favMovieRepository.checkIsFavorite(movieId) } returns rightValue

            val actualValue = favoriteUseCase.checkIsInFavorite(movieId, DataType.MOVIES)

            Assert.assertEquals(rightValue, actualValue)

            coVerify(exactly = 1) { favMovieRepository.checkIsFavorite(movieId) }
            confirmVerified(favMovieRepository)

            // perform check, if this test failed, it determines any invocation of this mocks on unFavorite
            confirmVerified(favTvShowRepository)
        }
    }

    @Test
    fun `checkIsInFavorite(tvshow) must call to tvshow favorite repository and return right value`() {
        runBlocking {
            val movieId = 1
            val rightValue = true

            coEvery { favTvShowRepository.checkIsFavorite(movieId) } returns rightValue

            val actualValue = favoriteUseCase.checkIsInFavorite(movieId, DataType.TVSHOW)

            Assert.assertEquals(rightValue, actualValue)

            coVerify(exactly = 1) { favTvShowRepository.checkIsFavorite(movieId) }
            confirmVerified(favTvShowRepository)

            // perform check, if this test failed, it determines any invocation of this mocks on unFavorite
            confirmVerified(favMovieRepository)
        }
    }

    @Test
    fun `saveFavoriteMovie() must call repository and return right value`() {
        runBlocking {
            val fakeData = FakeDomain.generateDetailMovieDomain(1)
            favoriteUseCase.saveFavoriteMovie(fakeData)

            coVerify(exactly = 1) { favMovieRepository.save(fakeData) }
            confirmVerified(favMovieRepository)
        }
    }

    @Test
    fun `saveFavoriteTvShow() must call repository and return right value`() {
        runBlocking {
            val fakeData = FakeDomain.generateDetailTvDomain(1)
            favoriteUseCase.saveFavoriteTvShow(fakeData)

            coVerify(exactly = 1) { favTvShowRepository.save(fakeData) }
            confirmVerified(favTvShowRepository)
        }
    }

    @Test
    fun `unFavorite(movie) must call to movie favorite`() {
        runBlocking {
            val movieId = 1
            favoriteUseCase.unFavorite(movieId, DataType.MOVIES)

            coVerify(exactly = 1) { favMovieRepository.unFavorite(movieId) }
            confirmVerified(favMovieRepository)

            // perform check, if this test failed, it determines any invocation of this mocks on unFavorite
            confirmVerified(favTvShowRepository)
        }
    }

    @Test
    fun `unFavorite(tvshow) must call to tvshow favorite repository and return right value`() {
        runBlocking {
            val movieId = 1
            favoriteUseCase.unFavorite(movieId, DataType.TVSHOW)

            coVerify(exactly = 1) { favTvShowRepository.unFavorite(movieId) }
            confirmVerified(favTvShowRepository)

            // perform check, if this test failed, it determines any invocation of this mocks on unFavorite
            confirmVerified(favMovieRepository)
        }
    }
}