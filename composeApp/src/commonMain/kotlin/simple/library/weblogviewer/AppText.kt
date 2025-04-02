package simple.library.weblogviewer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.compose.resources.Font
import web_log_viewer.composeapp.generated.resources.Res
import web_log_viewer.composeapp.generated.resources.lan_ting_regular

@Composable
fun AppText(
    text: String,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(Res.font.lan_ting_regular)),
        color = color,
        fontSize = fontSize
    )
}