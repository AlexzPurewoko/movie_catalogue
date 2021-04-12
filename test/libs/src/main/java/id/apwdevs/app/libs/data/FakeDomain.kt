package id.apwdevs.app.libs.data

import id.apwdevs.app.core.domain.model.DetailMovie
import id.apwdevs.app.core.domain.model.DetailTvShow
import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow
import id.apwdevs.app.core.domain.model.detail.EpisodeToAir

object FakeDomain {

    fun generateTvShowDomain(id: Int): TvShow {
        return TvShow(
            id, "", "", "",
            "", listOf(), "", "",
            0.0, 0
        )
    }

    fun generateMovieDomain(id: Int): Movies {
        return Movies(id, "", "", "", listOf(), "", "", "", 0.0, 0, false)
    }


    fun generateListMovieDomains(): List<Movies> {
        val mList = mutableListOf<Movies>()
        for (i in 1..5) {
            mList.add(generateMovieDomain(i))
        }
        return mList
    }

    fun generateListTvDomains(): List<TvShow> {
        val mList = mutableListOf<TvShow>()
        for (i in 1..5) {
            mList.add(generateTvShowDomain(i))
        }
        return mList
    }

    fun generateDetailMovieDomain(id: Int): DetailMovie {
        return DetailMovie(
            id, "", "", "", 0, 0.0, 0, 0,
            "", "", 0, "", "", 0.0, "",
            false, "", "", listOf(), listOf()
        )
    }

    fun generateEpisodeToAir(): EpisodeToAir {
        return EpisodeToAir(
            0, "", "", "", 0,
            0.0, "", 0, "", 0
        )
    }

    fun generateDetailTvDomain(id: Int): DetailTvShow {
        val episodeToAir = generateEpisodeToAir()
        return DetailTvShow(
            id, "", 0, "", "", 0.0, 0,
            0, "", "", "", "", 0.0, "", "",
            false, "", "", "", listOf(), listOf(), listOf(), episodeToAir, null, listOf()
        )
    }
}