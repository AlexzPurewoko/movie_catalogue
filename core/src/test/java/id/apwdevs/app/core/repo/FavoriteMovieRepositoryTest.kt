package id.apwdevs.app.core.repo

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.repository.FavoriteRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteMovieRepositoryTest {

    private lateinit var favoriteRepo: FavoriteRepository<Movies, DetailMovie>

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun getAllFavorites_must_return_data_if_any() {
    }

    @Test
    fun getFavoriteDetailItem_must_return_data_if_any() {
    }

    @Test
    fun checkIsFavorite_should_return_true_when_item_is_occur() {
    }

    @Test
    fun checkIsFavorite_should_return_false_if_no_matched_elements() {
    }

    @Test
    fun save_should_success_when_saving_data() {
    }

    @Test
    fun unFavorite_should_sucess_when_deleting_one_favorited() {
    }

}