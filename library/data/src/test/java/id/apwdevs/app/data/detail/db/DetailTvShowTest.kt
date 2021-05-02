package id.apwdevs.app.data.detail.db

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import id.apwdevs.app.libs.data.stub.genres
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.local.room.dao.FavTvShowDao
import id.apwdevs.app.libs.data.DetailTvShowDBStub
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailTvShowTest {
    private lateinit var dao: FavTvShowDao
    private lateinit var db: AppDatabase

    @Before
    fun composeDb() {
        val ctx =  ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        dao = db.favTvShowDetail()

        // prepare for all tests

        runBlocking {
            db.genreDao().insertGenres(genres())
        }
    }

    @After
    fun release() {
        db.close()
    }

    @Test
    fun should_return_correct_data_when_try_to_add_favorite_tvshow() {
        runBlocking {
            val data = listOf(
                DetailTvShowDBStub.favDetailTvShow(1),
                DetailTvShowDBStub.favDetailTvShow(2),
            )

            data.forEach { dao.insertFavDetailTvShow(it) }

            Assert.assertEquals(DetailTvShowDBStub.favDetailTvShow(2), dao.getTvShow(2))
            Assert.assertEquals(DetailTvShowDBStub.favDetailTvShow(1), dao.getTvShow(1))
        }
    }

    @Test
    fun should_success_when_get_added_data_from_favorite() {
        runBlocking {
            val data = DetailTvShowDBStub.favDetailTvShow()
            dao.insertFavDetailTvShow(data)

            Assert.assertTrue(dao.isTvShowExists(1))

        }
    }

    @Test
    fun should_success_when_try_to_delete_favorite_tvshow() {
        runBlocking {
            val data = DetailTvShowDBStub.favDetailTvShow()
            dao.insertFavDetailTvShow(data)
            Assert.assertTrue(dao.isTvShowExists(1))

            dao.deleteById(1)
            Assert.assertFalse(dao.isTvShowExists(1))
        }
    }

    @Test
    fun should_success_adding_more_data() {
        runBlocking {
            val data = listOf(
                DetailTvShowDBStub.favDetailTvShow(1),
                DetailTvShowDBStub.favDetailTvShow(2),
                DetailTvShowDBStub.favDetailTvShow(3),
            )

            data.forEach { dao.insertFavDetailTvShow(it) }
            Assert.assertEquals(data.size.toLong(), dao.getCount())
        }
    }


    @Test(expected = SQLiteConstraintException::class)
    fun should_failed_if_try_to_add_same_tvshow() {
        runBlocking {
            val data = DetailTvShowDBStub.favDetailTvShow()
            dao.insertFavDetailTvShow(data)
            dao.insertFavDetailTvShow(data)
        }
    }

    @Test
    fun should_failed_when_try_to_get_unexisting_tvshow() {
        runBlocking {
            Assert.assertNull(dao.getTvShow(1))
        }
    }

    @Test
    fun should_return_false_if_tvshow_is_not_exists() {
        runBlocking {
            Assert.assertFalse(dao.isTvShowExists(1))
        }
    }

}
