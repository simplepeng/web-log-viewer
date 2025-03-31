package simple.library.weblogviewer

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Message(
    val time: Long,
    val level: Int,
    //
    val tag: String,
    val message: String,
    var text: String,
) {
    companion object {
        const val LEVEL_VERBOSE = 0
        const val LEVEL_DEBUG = 1
        const val LEVEL_INFO = 2
        const val LEVEL_WARN = 3
        const val LEVEL_ERROR = 4

        fun parse(text: String): Message? {
            return Json.decodeFromString<Message>(text).apply {
                this.text = text
            }
        }
    }
}