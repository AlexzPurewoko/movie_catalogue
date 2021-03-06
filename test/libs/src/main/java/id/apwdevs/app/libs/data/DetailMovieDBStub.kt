package id.apwdevs.app.libs.data

import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity
import id.apwdevs.app.libs.data.stub.genres
import id.apwdevs.app.libs.data.stub.productionCompanies

object DetailMovieDBStub {
    private fun composeFavDetailMovieEntity(id: Int): FavDetailMovieEntity {
        return FavDetailMovieEntity(
            id, "", "movie", "", 0, 0.0,
            0, 0, "", "", 0, "",
            "", 0.0, "", false, "", ""
        )
    }

    fun favDetailMovie(id: Int = 1): FavDetailMovie {
        return FavDetailMovie(
            composeFavDetailMovieEntity(id),
            genres(),
            productionCompanies()
        )
    }
}