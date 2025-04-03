package simple.library.weblogviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class MainViewModel : ViewModel() {

    private var client: HttpClient? = null

    private val _messageList = MutableStateFlow<List<Message>>(emptyList())
    val messageList = _messageList.asStateFlow()

    fun addMessage(message: Message) {
        _messageList.value += message
    }

    fun connect(ip: String, port: String) {
        println("connect")
        client = HttpClient() {
            install(WebSockets) {
                pingIntervalMillis = 1000
            }
        }
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            addMessage(Message(Clock.System.now().toEpochMilliseconds(), Message.LEVEL_ERROR, "connect", exception.message ?: ""))
        }
        viewModelScope.launch(exceptionHandler) {
            client?.webSocket(method = HttpMethod.Get, host = ip, port = port.toInt(), path = "/") {
                while (true) {
                    val text = (incoming.receive() as Frame.Text).readText()
                    println("text = $text")
                    Message.parse(text)?.let {
                        addMessage(it)
                    }
                }
            }
        }
    }

    fun close() {
        client?.close()
    }

    fun clear() {
        _messageList.value = emptyList()
    }

    fun addTestMessage() {
        val time = Clock.System.now().toEpochMilliseconds()
        addMessage(Message(time, Message.LEVEL_VERBOSE, "tag", "message"))
        addMessage(Message(time, Message.LEVEL_DEBUG, "tag", "message"))
        addMessage(Message(time, Message.LEVEL_INFO, "tag", "message"))
        addMessage(Message(time, Message.LEVEL_WARN, "tag", "message"))
        addMessage(Message(time, Message.LEVEL_ERROR, "tag", "message"))
    }
}