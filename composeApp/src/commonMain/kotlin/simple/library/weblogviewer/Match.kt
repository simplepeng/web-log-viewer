package simple.library.weblogviewer

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val text: String,
    val startIndex: Int,
    val endIndex: Int
)
