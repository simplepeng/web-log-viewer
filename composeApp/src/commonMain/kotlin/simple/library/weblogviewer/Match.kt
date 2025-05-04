package simple.library.weblogviewer

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val text: String,
    val start: Int,
    val end: Int
)
