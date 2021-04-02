package id.apwdevs.app.data.source.local.entity.converters

import androidx.room.TypeConverter

class ListStrCommaConverter {
    @TypeConverter
    fun fromListToStr(lists: Data): String {
        var str = ""
        for(item in lists.data) {
            str+="$item,"
        }
        str = str.removeSuffix(",")
        return str
    }

    @TypeConverter
    fun fromStrToList(str: String): Data {
        return Data(str.split(",").toList())
    }

    data class Data(
        val data: List<String>
    )
}