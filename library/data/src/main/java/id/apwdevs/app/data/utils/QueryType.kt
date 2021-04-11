package id.apwdevs.app.data.utils

sealed class QueryType {
    class SEARCH(val query: String, val includeAdult: Boolean) : QueryType()
    class POPULAR : QueryType()
}
