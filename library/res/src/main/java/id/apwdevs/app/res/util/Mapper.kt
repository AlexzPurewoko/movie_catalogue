package id.apwdevs.app.res.util

import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.res.data.MovieShowItem

@JvmName("mapMovieDomainToMovieShowItem")
fun Movies.mapToItem() =
    MovieShowItem(
       movieId,
       title,
       overview,
       backdropPath,
       genres,
       voteCount,
       voteAverage
    )

@JvmName("mapTvShowDomainToMovieShowItem")
fun TvShow.mapToItem() =
    MovieShowItem(
        tvId,
        name,
        overview,
        backdropPath,
        genres,
        voteCount,
        voteAverage
    )