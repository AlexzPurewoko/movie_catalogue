package id.apwdevs.app.core.repo

import android.content.Context
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import id.apwdevs.app.core.data.FakeDomain
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import id.apwdevs.app.core.repository.FavoriteTvShowRepoImpl
import id.apwdevs.app.core.utils.DomainToEntityMapper
import id.apwdevs.app.core.utils.State
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShow
import id.apwdevs.app.data.source.local.entity.detail.tvshow.FavDetailTvShowEntity
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.local.room.dbcase.FavoriteDataSource
import id.apwdevs.app.data.source.local.room.dbcase.FavoriteTvShowDataSource
import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteTvShowRepositoryTest {

    private val context: Context by lazy {
        ApplicationProvider.getApplicationContext()
    }

    private lateinit var appDb: AppDatabase

    private lateinit var favDataSource: FavoriteDataSource<FavDetailTvShowEntity, FavDetailTvShow>
    private lateinit var favoriteRepo: FavoriteRepository<TvShow, DetailTvShow>

    @Before
    fun setup() {
        appDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        favDataSource = spyk(FavoriteTvShowDataSource(appDb))
        favoriteRepo = FavoriteTvShowRepoImpl(favDataSource)
    }

    @After
    fun tearDown() {
        appDb.close()
    }

    @Test
    fun getAllFavorites_must_return_data_if_any() {
        runBlocking {
            // first, save one fav to database
            val fakeData = FakeDomain.generateDetailTvDomain(1)
            val fakeResults = listOf(FakeDomain.generateTvShowDomain(1))
            favoriteRepo.save(fakeData)

            // secondly, tests
            val favResults = favoriteRepo.getAllFavorites()
            val observer = mockk<Observer<State<List<TvShow>>>>(relaxUnitFun = true)

            var results: List<TvShow>? = null
            favResults.collect {
                observer.onChanged(it)
                if (it is State.Success) {
                    results = it.data
                }
            }
            delay(1000)

            Assert.assertNotNull(results)
            Assert.assertEquals(fakeResults.size, results?.size)
            Assert.assertEquals(fakeResults[0].tvId, results?.get(0)?.tvId)

            coVerifyOrder {

                favDataSource.save(DomainToEntityMapper.favDetailTvShow(fakeData))
                observer.onChanged(match {
                    it is State.Loading
                })

                favDataSource.getAllFavorite()
                favDataSource.genres()
                favDataSource.genreMapper(1)

                observer.onChanged(match {
                    it is State.Success && it.data == results
                })
            }
            confirmVerified(observer, favDataSource)
        }
    }

    @Test
    fun getFavoriteDetailItem_must_return_data_if_any() {
        runBlocking {
            // first, save one fav to database
            val fakeData = FakeDomain.generateDetailTvDomain(1)
            favoriteRepo.save(fakeData)

            // secondly, tests
            val favResults = favoriteRepo.getFavoriteDetailItem(1)
            val observer = mockk<Observer<State<DetailTvShow>>>(relaxUnitFun = true)

            var results: DetailTvShow? = null
            favResults.collect {
                observer.onChanged(it)
                if (it is State.Success) {
                    results = it.data
                }
            }
            delay(1000)

            Assert.assertNotNull(results)
            Assert.assertEquals(fakeData.id, results?.id)

            coVerifyOrder {

                favDataSource.save(DomainToEntityMapper.favDetailTvShow(fakeData))
                observer.onChanged(match {
                    it is State.Loading
                })

                favDataSource.getFavorite(1)

                observer.onChanged(match {
                    it is State.Success && it.data == results
                })
            }

            confirmVerified(observer, favDataSource)
        }
    }

    @Test
    fun checkIsFavorite_should_return_true_when_item_is_occur() {
        runBlocking {
            val fakeData = FakeDomain.generateDetailTvDomain(1)
            favoriteRepo.save(fakeData)

            val isFav = favoriteRepo.checkIsFavorite(1)
            Assert.assertTrue(isFav)
            coVerify { favDataSource.isFavorite(1) }
        }
    }

    @Test
    fun checkIsFavorite_should_return_false_if_no_matched_elements() {
        runBlocking {
            val isFav = favoriteRepo.checkIsFavorite(1)
            Assert.assertFalse(isFav)
            coVerify { favDataSource.isFavorite(1) }
        }
    }

    @Test
    fun save_should_success_when_saving_data() {
        runBlocking {
            val fakeData = FakeDomain.generateDetailTvDomain(1)
            val fakeMapper = DomainToEntityMapper.favDetailTvShow(fakeData)
            favoriteRepo.save(fakeData)

            val isFav = favoriteRepo.checkIsFavorite(1)
            Assert.assertTrue(isFav)

            coVerify { favDataSource.save(fakeMapper) }
        }
    }

    @Test
    fun unFavorite_should_sucess_when_deleting_one_favorited() {
        runBlocking {
            val fakeData = FakeDomain.generateDetailTvDomain(1)
            favoriteRepo.save(fakeData)

            var isFav = favoriteRepo.checkIsFavorite(1)
            Assert.assertTrue(isFav)

            favoriteRepo.unFavorite(1)
            isFav = favoriteRepo.checkIsFavorite(1)
            Assert.assertFalse(isFav)
            coVerify { favDataSource.deleteData(1) }
        }
    }

}