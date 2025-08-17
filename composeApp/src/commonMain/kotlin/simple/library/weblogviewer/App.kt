package simple.library.weblogviewer

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App(
   hostName: String = "",
   port: String = "",
) {
   MaterialTheme {
      MainScreen(hostName, port)
   }
}