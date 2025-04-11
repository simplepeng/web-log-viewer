package simple.library.weblogviewer

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() =
//    application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "web-log-viewer",
//    ) {
//        DevelopmentEntryPoint {
//            App()
//        }
//    }
    singleWindowApplication(
        title = "My CHR App",
        state = WindowState(width = 800.dp, height = 800.dp),
        alwaysOnTop = true
    ) {
        DevelopmentEntryPoint {
            App()
        }
    }
