package simple.library.weblogviewer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    //#

    private val _tagInput = MutableStateFlow("")
    var tagInput = _tagInput.asStateFlow()

    fun setTagInput(value: String) {
        _tagInput.value = value
        filterMessage()
    }

    //#

    private val allMessage = mutableListOf<Message>()

    private val _messageList = MutableStateFlow<List<Message>>(emptyList())
    val messageList = _messageList.asStateFlow()

    fun addMessage(message: Message) {
//        _messageList.value += message
        allMessage.add(message)
//        _messageList.value = allMessage.filter { it.tag == tagInput.value }
        filterMessage()
    }

    //#

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
        addMessage(Message(time, Message.LEVEL_VERBOSE, "tag", "hello java"))
        addMessage(Message(time, Message.LEVEL_DEBUG, "tag1", "hello kotlin"))
        addMessage(Message(time, Message.LEVEL_INFO, "tag2", "hello rust"))
        addMessage(Message(time, Message.LEVEL_WARN, "tag", "hello swift"))
        addMessage(Message(time, Message.LEVEL_ERROR, "tag3", "hello arkTs"))
    }

    fun filterMessage() {
        _messageList.value = if (tagInput.value.isEmpty()) allMessage else allMessage.filter { it.tag == tagInput.value }
    }

    //#
    private val _highLightInput = MutableStateFlow("")
    var highLightInput = _highLightInput.asStateFlow()

    fun setHighLightInput(value: String) {
        _highLightInput.value = value
        if (value.isNotEmpty()) {
            _messageList.update {
                it.map { message ->
                    message.highLightList = value.split(" ")
                    message.matchList = mutableListOf()
                    if (message.highLightList.isNotEmpty()) {
                        message.highLightList.forEach { highLight ->
                            var lastIndex = 0
                            var startIndex = message.message.indexOf(highLight, lastIndex)
                            while (startIndex >= 0) {
                                val endIndex = startIndex + highLight.length
                                message.matchList += Match(highLight, startIndex, endIndex)
                                lastIndex = endIndex
                                startIndex = message.message.indexOf(highLight, lastIndex)
                            }
                        }
                    }
                    if (message.matchList.isNotEmpty()) {
                        LogHelper.debug("matchList = ${message.matchList}")
                    }
                    message
                }
            }
        }
    }
}