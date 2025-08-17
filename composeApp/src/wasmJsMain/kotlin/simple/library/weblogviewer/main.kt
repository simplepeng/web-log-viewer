package simple.library.weblogviewer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
   ComposeViewport(document.body!!) {
      val hostName = document.location?.hostname.orEmpty()
      val port = document.location?.port.orEmpty()
      val search = document.location?.search.orEmpty()
      println("$hostName:$port$search")

      App(
         hostName = hostName,
         port = port,
      )
   }
}