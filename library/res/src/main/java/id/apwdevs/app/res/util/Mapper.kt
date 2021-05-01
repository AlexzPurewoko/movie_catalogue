package id.apwdevs.app.res.util

import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.res.data.MovieShowItem

object Mapper {
    fun mapDomainMovieToMovieShowItem(movie: Movies) =
        MovieShowItem(
            movie.movieId,
            movie.title,
            movie.overview,
            movie.backdropPath,
            movie.genres,
            movie.voteCount,
            movie.voteAverage
        )

    fun mapDomainTvShowToMovieShowItem(tvshow: TvShow) =
        MovieShowItem(
            tvshow.tvId,
            tvshow.name,
            tvshow.overview,
            tvshow.backdropPath,
            tvshow.genres,
            tvshow.voteCount,
            tvshow.voteAverage
        )
}