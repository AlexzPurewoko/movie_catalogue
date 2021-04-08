package id.apwdevs.app.core.data

import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.PageResponse
import id.apwdevs.app.data.source.remote.response.TvShowItemResponse
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.parts.Genre
import id.apwdevs.app.data.source.remote.response.tvdetail.TvDetailResponse
import id.apwdevs.app.data.source.remote.response.tvdetail.parts.EpisodesToAir

object FakeDataDetail {
    fun generateMovieDetailResponse(id: Int): MovieDetailResponse {
        return MovieDetailResponse(
                "en", null, false, "hello", "/eee", 0, listOf(),
                0.0, listOf(), id, 0, 0, "", "", 0, "",
                listOf(), listOf(), "", 0.0, null, "", false, "",
                ""
        )
    }

    fun generateMovieItemResponse(id: Int): MovieItemResponse {
        return MovieItemResponse(
                "", "", "", false, "", listOf(1, 2), "", "",
                "", 0.0, 0.0, id, false, 0
        )
    }

    fun generateMovieResponse(query: String): PageResponse<MovieItemResponse> {
        val mList = mutableListOf<MovieItemResponse>()
        for (i in 1..5) {
            mList.add(generateMovieItemResponse(i))
        }
        return PageResponse(
                1, 1, mList, 5
        )
    }

    fun generateGenre(): List<Genres> {
        return listOf(
                Genres(1, "a"),
                Genres(1, "b")
        )
    }

    fun generateGenreResponse(): GenreResponse {
        return GenreResponse(listOf(
                Genre(id = 1, name = "a"),
                Genre(id = 1, name = "b")
        ))
    }

    // tv
    fun generateTvResponse(s: String): PageResponse<TvShowItemResponse> {
        val mList = mutableListOf<TvShowItemResponse>()
        for (i in 1..5) {
            mList.add(generateTvItemResponse(i))
        }
        return PageResponse(
                1, 1, mList, 5
        )
    }

    fun generateTvItemResponse(id: Int): TvShowItemResponse {
        return TvShowItemResponse(
                "", "", "", listOf(1, 2), "", listOf(), "", "",
                0.0, 0.0, "", id, 0,
        )
    }

    fun generateEpisodesToAir() =
            EpisodesToAir("", "", "", 0, 0.0, "", 0,
                    0, null, 0)

    fun generateTvDetailResponse(tvId: Int): TvDetailResponse {
        return TvDetailResponse(
                "en", 0, listOf(), "hello", "/eee", listOf(), 0.0,
                listOf(), tvId, 0, 0, "", "", listOf(), listOf(),
                listOf(), generateEpisodesToAir(), "", listOf(), listOf(), listOf(), "", 0.0,
                "", "", listOf(), generateEpisodesToAir(), false, "", "", ""
        )
    }

}