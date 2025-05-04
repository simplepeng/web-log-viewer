package simple.library.weblogviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageItem(
    msg: Message,
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box() {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                AppText(
                    text = buildAnnotatedString {
                        append(msg.message)
                    },
                    color = msg.color,
                    fontSize = 14.sp,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    AppText(
                        text = "tag: " + msg.tag,
                        color = msg.color,
                        fontSize = 10.sp,
                    )
                    AppText(
                        text = msg.timeText,
                        color = msg.color,
                        fontSize = 8.sp,
                    )
                }
            }
        }
    }
}