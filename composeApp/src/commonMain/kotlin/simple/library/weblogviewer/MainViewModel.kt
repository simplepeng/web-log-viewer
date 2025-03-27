package simple.library.weblogviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _messageList = MutableStateFlow<List<String>>(emptyList())
    val messageList = _messageList.asStateFlow()

    fun connect(ip: String, port: String) {
        val client = HttpClient(CIO) {
            install(WebSockets) {
                pingIntervalMillis = 1000
            }
        }
        viewModelScope.launch {
            client.webSocket(method = HttpMethod.Get, host = ip, port = port.toInt(), path = "/") {
                while (true) {
                    val message = incoming.receive() as Frame.Text
                    val text = message.readText()
                    println(text)
                }
            }
        }
    }
}