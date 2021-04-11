package id.apwdevs.app.data.db.detail.movie

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import id.apwdevs.app.data.db.detail.stub.genres
import id.apwdevs.app.data.source.local.room.AppDatabase
import id.apwdevs.app.data.source.local.room.dao.FavDetailMovieDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DetailMovieDBTest {
    private lateinit var dao: FavDetailMovieDao
    private lateinit var db: AppDatabase

    @Before
    fun composeDb() {
        val ctx =  ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        dao = db.favMovieDetail()

        val genreDao = db.genreDao()
        runBlocking {
            genreDao.insertGenres(genres())
        }
    }

    @After
    fun release() {
        db.close()
    }

    @Test fun should_return_correct_data_when_try_to_add_favorite_movie() {
        runBlocking {
            val data = listOf(
                DetailMovieDBStub.favDetailMovie(1),
                DetailMovieDBStub.favDetailMovie(2),
            )

            data.forEach { dao.insertFavDetailMovie(it) }

            Assert.assertEquals(DetailMovieDBStub.favDetailMovie(2), dao.getMovie(2))
            Assert.assertEquals(DetailMovieDBStub.favDetailMovie(1), dao.getMovie(1))
        }
    }

    @Test fun should_success_when_get_added_data_from_favorite() {
        runBlocking {
            val data = DetailMovieDBStub.favDetailMovie()
            dao.insertFavDetailMovie(data)

            Assert.assertTrue(dao.isMovieExists(1))

        }
    }

    @Test fun should_success_when_try_to_delete_favorite_movie() {
        runBlocking {
            val data = DetailMovieDBStub.favDetailMovie()
            dao.insertFavDetailMovie(data)
            Assert.assertTrue(dao.isMovieExists(1))

            dao.deleteById(1)
            Assert.assertFalse(dao.isMovieExists(1))
        }
    }

    @Test fun should_success_adding_more_data() {
        runBlocking {
            val data = listOf(
                DetailMovieDBStub.favDetailMovie(1),
                DetailMovieDBStub.favDetailMovie(2),
                DetailMovieDBStub.favDetailMovie(3),
            )

            data.forEach { dao.insertFavDetailMovie(it) }
            Assert.assertEquals(data.size.toLong(), dao.getCount())
        }
    }

    @Test fun should_success_retrieve_data_with_offset() {
        runBlocking {
            val data = listOf(
                DetailMovieDBStub.favDetailMovie(1),
                DetailMovieDBStub.favDetailMovie(2),
                DetailMovieDBStub.favDetailMovie(3),
                DetailMovieDBStub.favDetailMovie(4),
            )

            data.forEach { dao.insertFavDetailMovie(it) }

            val offsetTests: suspend (Int, Int, Int) -> Unit = { expectedSize, offsetStart, offsetEnd ->
                val d = dao.getDetailMovieEntity(offsetStart, offsetEnd)
                Assert.assertEquals(expectedSize, d.size)
            }
            offsetTests(4, 0, 4)
            offsetTests(1, 0, 1)
            offsetTests(2, 0, 2)
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun should_failed_if_try_to_add_same_movie() {
        runBlocking {
            val data = DetailMovieDBStub.favDetailMovie()
            dao.insertFavDetailMovie(data)
            dao.insertFavDetailMovie(data)
        }
    }

    @Test fun should_failed_when_try_to_get_unexisting_movie() {
        runBlocking {
            Assert.assertNull(dao.getMovie(1))
        }
    }

    @Test fun should_return_false_if_movie_is_not_exists() {
        runBlocking {
            Assert.assertFalse(dao.isMovieExists(1))
        }
    }

}

