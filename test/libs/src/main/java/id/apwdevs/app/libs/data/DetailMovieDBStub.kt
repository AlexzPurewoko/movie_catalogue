package id.apwdevs.app.libs.data

import id.apwdevs.app.data.db.detail.stub.genres
import id.apwdevs.app.data.db.detail.stub.productionCompanies
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovie
import id.apwdevs.app.data.source.local.entity.detail.movie.FavDetailMovieEntity

object DetailMovieDBStub {
    fun composeFavDetailMovieEntity(id: Int) : FavDetailMovieEntity {
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