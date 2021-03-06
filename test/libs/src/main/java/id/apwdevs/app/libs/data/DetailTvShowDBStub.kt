package id.apwdevs.app.libs.data

import id.apwdevs.app.data.source.local.entity.detail.tvshow.*
import id.apwdevs.app.libs.data.stub.genres
import id.apwdevs.app.libs.data.stub.productionCompanies


object DetailTvShowDBStub {
    private fun composeFavDetailTvShowEntity(id: Int): FavDetailTvShowEntity {
        return FavDetailTvShowEntity(
            id, "", 0, "", "", 0.0,
            0, 0, "", "", "", "",
            0.0, "", "", false, "", "",
            ""
        )
    }

    private fun creators(tvId: Int): List<CreatedByEntity> {
        return listOf(
            CreatedByEntity(tvId, tvId, 1, "", "a", ""),
            CreatedByEntity(tvId * 10, tvId, 2, "", "a", "")
        )
    }

    private fun episodes(tvId: Int): List<EpisodesToAir> {
        return listOf(
            EpisodesToAir(
                tvId, tvId, "", "",
                "", 1, 0.0, "", 1, "",
                0, EpisodesToAir.AiringType.LAST_AIR
            ),
            EpisodesToAir(
                tvId * 10, tvId, "", "",
                "", 1, 0.0, "", 2, "",
                0, EpisodesToAir.AiringType.NEXT_AIR
            ),
        )
    }

    private fun seasons(tvId: Int): List<TvShowSeason> {
        return listOf(
            TvShowSeason(tvId, tvId, "", "", 1, "", 1, ""),
            TvShowSeason(tvId * 10, tvId, "", "", 1, "", 2, ""),
        )
    }

    fun favDetailTvShow(id: Int = 1): FavDetailTvShow {
        return FavDetailTvShow(
            composeFavDetailTvShowEntity(id),
            genres(),
            productionCompanies(),
            creators(id),
            episodes(id),
            seasons(id)
        )
    }
}