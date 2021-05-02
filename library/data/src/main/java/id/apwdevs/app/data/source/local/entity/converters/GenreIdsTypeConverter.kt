package id.apwdevs.app.data.source.local.entity.converters

import androidx.room.TypeConverter

@Suppress("unused")
class GenreIdsTypeConverter {
    @TypeConverter
    fun fromGenresToString(genres: GenreIdData) : String {
        var str = ""
        for(genreInt in genres.data) {
            str+="$genreInt,"
        }
        str = str.removeSuffix(",")
        return str
    }

    @TypeConverter
    fun fromStrToGenres(strIdGenre: String): GenreIdData {
        val ret = mutableListOf<Int>()
        if(strIdGenre.isNotEmpty())
            strIdGenre.split(",").forEach { numStr -> ret.add(numStr.toInt()) }

        return GenreIdData(ret)
    }

    data class GenreIdData(
        val data: List<Int>
    )
}