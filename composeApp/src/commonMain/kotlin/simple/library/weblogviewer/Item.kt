package simple.library.weblogviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageItem(
    msg: Message,
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box() {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                AppText(
                    text = msg.timeText,
                    color = msg.color,
                )
                AppText(
                    text = msg.tag,
                    color = msg.color,
                )
                AppText(
                    text = msg.message,
                    color = msg.color,
                )
            }
        }
    }
}