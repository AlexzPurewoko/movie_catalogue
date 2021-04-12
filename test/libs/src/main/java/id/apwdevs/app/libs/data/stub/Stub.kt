package id.apwdevs.app.data.db.detail.stub

import id.apwdevs.app.data.source.local.entity.Genres
import id.apwdevs.app.data.source.local.entity.detail.ProductionCompanies

fun genres(): List<Genres> {
    return listOf(
        Genres(1, "a"),
        Genres(2, "b")
    )
}

fun productionCompanies(): List<ProductionCompanies> {
    return listOf(
        ProductionCompanies(1, "", "a", ""),
        ProductionCompanies(2, "", "b", "")
    )
}