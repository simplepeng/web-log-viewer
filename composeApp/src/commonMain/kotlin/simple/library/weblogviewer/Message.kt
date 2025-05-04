package simple.library.weblogviewer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
    val key: String
        get() = "${time}_${tag}-${message}"

    var text: String = ""
    var highLightList: List<String> = mutableListOf()
    var matchList: MutableList<Match> = mutableListOf()

    val timeText: String
        get() {
            val instant = Instant.fromEpochMilliseconds(time)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
//            return "${localDateTime.year}-${localDateTime.monthNumber}-${localDateTime.dayOfMonth} ${localDateTime.hour}:${localDateTime.minute}:${localDateTime.second}"
            return "${localDateTime.hour.toString().padStart(2, '0')}:${
                localDateTime.minute.toString().padStart(2, '0')
            }:${localDateTime.second.toString().padStart(2, '0')}"
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

    val annotatedString: AnnotatedString
        get() {
            return buildAnnotatedString {
                if (matchList.isEmpty()) {
                    append(message)
                    return@buildAnnotatedString
                }
                var lastIndex = 0
                matchList.sortedBy { it.startIndex }.forEachIndexed { index, match ->
                    if (match.startIndex == 0) {
                        withStyle(SpanStyle(color = Color.Red)) {
                            append(match.text)
                        }
                    } else {
                        message.substring(lastIndex, match.startIndex).let {
                            withStyle(SpanStyle(color = color)) {
                                append(it)
                            }
                        }
                        withStyle(SpanStyle(color = Color.Red)) {
                            append(match.text)
                        }
                    }
                    lastIndex = match.endIndex
                    if (index == matchList.size - 1 && lastIndex < message.length) {
                        withStyle(SpanStyle(color = color)) {
                            message.substring(lastIndex, message.length).let {
                                append(it)
                            }
                        }
                    }
                }
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