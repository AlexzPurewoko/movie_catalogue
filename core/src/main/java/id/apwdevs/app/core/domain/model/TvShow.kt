package id.apwdevs.app.core.domain.model

data class TvShow(
   val tvId: Int,
   val name: String,
   val firstAirDate: String,
   val overview: String,
   val language: String,
   val genres: List<Genre>,
   val posterPath: String?,
   val originCountry: List<String>,
   val backdropPath: String?,
   val voteAverage: Double,
   val voteCount: Int
)