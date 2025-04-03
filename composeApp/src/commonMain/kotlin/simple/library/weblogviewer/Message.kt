package simple.library.weblogviewer

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Message(
    val time: Long,
    val level: Int,
    //
    val tag: String,
    val message: String,
) {
    var text: String = ""

    val timeText: String
        get() {
            val instant = Instant.fromEpochMilliseconds(time)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
//            return "${localDateTime.year}-${localDateTime.monthNumber}-${localDateTime.dayOfMonth} ${localDateTime.hour}:${localDateTime.minute}:${localDateTime.second}"
            return "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}:${localDateTime.second.toString().padStart(2, '0')}"
        }

    val color: Color
        get() {
            return when (level) {
                LEVEL_VERBOSE -> Color.Gray
                LEVEL_DEBUG -> Color.Blue
                LEVEL_INFO -> Color(0, 255, 0)
                LEVEL_WARN -> Color(255, 193, 37)
                LEVEL_ERROR -> Color.Red
                else -> Color.Black
            }
        }

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