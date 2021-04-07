package id.apwdevs.app.core.data

import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.remote.response.GenreResponse
import id.apwdevs.app.data.source.remote.response.MovieItemResponse
import id.apwdevs.app.data.source.remote.response.PageResponse
import id.apwdevs.app.data.source.remote.response.moviedetail.MovieDetailResponse
import id.apwdevs.app.data.source.remote.response.parts.Genre

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
        return PageResponse<MovieItemResponse>(
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
}