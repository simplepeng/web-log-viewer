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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class MainViewModel : ViewModel() {

    private var client: HttpClient? = null

    private var isConnected = false

    //#

    private val _tagInput = MutableStateFlow("")
    var tagInput = _tagInput.asStateFlow()

    fun setTagInput(value: String) {
        _tagInput.value = value
        filterMessage()
    }

    //#

    private val _allMessage = MutableStateFlow<List<Message>>(mutableListOf())

    private val _messageList = MutableStateFlow<List<Message>>(emptyList())
    val messageList = _messageList.asStateFlow()

    fun addMessage(message: Message) {
        _allMessage.value += message
        filterMessage()
        paddingHighLightAndMatch()
    }

    fun addTextMessage(
        message: String,
        level: Int = Message.LEVEL_VERBOSE,
        tag: String = "web-log",
    ) {
        addMessage(
            Message(
                time = Clock.System.now().toEpochMilliseconds(),
                level = level,
                tag = tag,
                message = message,
            )
        )
    }

    fun addErrorTextMessage(
        message: String,
        tag: String = "web-log",
    ) {
        addTextMessage(
            message = message,
            tag = tag,
            level = Message.LEVEL_ERROR,
        )
    }

    //#

    fun connect(ip: String, port: String) {
//        println("connect")
        if (ip.isEmpty() || port.isEmpty()) {
            addTextMessage(
                level = Message.LEVEL_ERROR,
                message = "ip或port不能为空"
            )
            return
        }

        if (client != null && isConnected) {
            addTextMessage("WebSocket连接已建立")
            return
        }
        client = HttpClient() {
            install(WebSockets) {
                pingIntervalMillis = 1000
            }
        }
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            addErrorTextMessage(
                message = "WebSocket连接失败 -- ${exception.message}"
            )
            isConnected = false
        }
        viewModelScope.launch(exceptionHandler) {
            client?.webSocket(method = HttpMethod.Get, host = ip, port = port.toInt(), path = "/") {
                addTextMessage(message = "WebSocket连接成功", level = Message.LEVEL_DEBUG)
                isConnected = true
                while (true) {
                    val text = (incoming.receive() as Frame.Text).readText()
//                    println("text = $text")
                    Message.parse(text)?.let {
                        addMessage(it)
                    }
                }
                isConnected = false
            }
        }
    }

    fun close() {
        client?.close()
        client = null
        addErrorTextMessage("连接已断开")
    }

    fun clear() {
        _allMessage.value = emptyList()
        _messageList.value = _allMessage.value
    }

    fun addTestMessage() {
        val time = Clock.System.now().toEpochMilliseconds()
        val level = listOf(Message.LEVEL_ERROR, Message.LEVEL_WARN, Message.LEVEL_INFO, Message.LEVEL_DEBUG, Message.LEVEL_VERBOSE).random()
        val name = listOf("java", "kotlin", "rust", "swift", "arkTs", "c", "c++", "c#", "php", "go", "python", "ruby").random()
        addMessage(
            Message(
                time = time,
                level = level,
                tag = "tag${(0..10).random()}",
                message = "hello world : ${name}"
            )
        )
        LogHelper.debug("allMessage = $_allMessage")
//        addMessage(Message(time = Clock.System.now().toEpochMilliseconds(), Message.LEVEL_VERBOSE, "tag", "hello java"))
//        addMessage(Message(time = Clock.System.now().toEpochMilliseconds(), Message.LEVEL_DEBUG, "tag1", "hello kotlin"))
//        addMessage(Message(time = Clock.System.now().toEpochMilliseconds(), Message.LEVEL_INFO, "tag2", "hello rust"))
//        addMessage(Message(time = Clock.System.now().toEpochMilliseconds(), Message.LEVEL_WARN, "tag", "hello swift"))
//        addMessage(Message(time = Clock.System.now().toEpochMilliseconds(), Message.LEVEL_ERROR, "tag3", "hello arkTs"))
    }

    fun filterMessage() {
        _messageList.value = if (tagInput.value.isEmpty())
            _allMessage.value
        else
            _allMessage.value.filter { it.tag == tagInput.value }
    }

    private val _delimiter = MutableStateFlow("/")
    val delimiter = _delimiter.asStateFlow()

    fun setDelimiter(value: String) {
        _delimiter.value = value
    }

    //#
    private val _highLightInput = MutableStateFlow("")
    var highLightInput = _highLightInput.asStateFlow()

    fun setHighLightInput(value: String) {
        _highLightInput.value = value
        paddingHighLightAndMatch()
    }

    private fun paddingHighLightAndMatch() {
        _messageList.update {
            it.map { message ->
                val newMessage = message.copy(
                    highLightList = _highLightInput.value.split(_delimiter.value).filter { it.isNotEmpty() },
                    //                    matchList = mutableListOf()
                )
                newMessage.matchList.clear()
                if (newMessage.highLightList.isNotEmpty()) {
                    LogHelper.debug("highLightList = ${message.highLightList}")
                    newMessage.highLightList.forEach { highLight ->
                        if (highLight.isNotEmpty()) {
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
                }
                if (newMessage.matchList.isNotEmpty()) {
                    LogHelper.debug("matchList = ${message.matchList}")
                }
                newMessage
            }
        }
    }
}