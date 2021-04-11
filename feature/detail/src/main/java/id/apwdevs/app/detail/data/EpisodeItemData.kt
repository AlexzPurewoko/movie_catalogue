package id.apwdevs.app.detail.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeItemData(
    val image: String?,
    val title: String,
    val vote: Double,
    val date: String,
    val seasonNumber: Int
) : Parcelable