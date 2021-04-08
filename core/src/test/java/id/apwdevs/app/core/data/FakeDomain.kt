package id.apwdevs.app.core.data

import id.apwdevs.app.core.domain.model.Movies
import id.apwdevs.app.core.domain.model.TvShow

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
}