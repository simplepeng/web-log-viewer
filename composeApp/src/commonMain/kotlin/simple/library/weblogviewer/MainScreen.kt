package simple.library.weblogviewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import web_log_viewer.composeapp.generated.resources.Res
import web_log_viewer.composeapp.generated.resources.ic_clear

@Composable
fun MainScreen(
//    viewModel: MainViewModel = viewModel()
//    viewModel: MainViewModel = MainViewModel()
) {

    val viewModel = remember { MainViewModel() }
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("8080") }
    val tagInput by viewModel.tagInput.collectAsState()
    val highLightInput by viewModel.highLightInput.collectAsState()
    val delimiterInput by viewModel.delimiter.collectAsState()

//    LaunchedEffect(tagInput) {
//        viewModel.filterMessage(tagInput)
//    }

    val messageList by viewModel.messageList.collectAsState()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(messageList) {
        if (messageList.isNotEmpty()) {
            lazyListState.animateScrollToItem(messageList.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth(if (isWeb) 1f else 1f)
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            //
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                OutlinedTextField(
                    value = ip,
                    onValueChange = {
                        if (it.isEmpty() || it.all { it.isDigit() || it == '.' }) {
                            ip = it
                        }
                    },
                    label = { Text("ip") },
//                    modifier = if (getPlatform().isWeb) Modifier else Modifier.weight(0.5f),
                    modifier = Modifier.weight(0.5f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal
                    ),
                    placeholder = {
                        Text(
                            text = "手机在同一局域网的ip",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                OutlinedTextField(
                    value = port,
                    onValueChange = {
                        if (it.isEmpty() || it.all { it.isDigit() }) {
                            port = it
                        }
                    },
                    label = { Text("port") },
//                    modifier = if (getPlatform().isWeb) Modifier else Modifier.weight(0.2f),
                    modifier = Modifier.weight(0.25f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                OutlinedTextField(
                    value = tagInput,
                    onValueChange = { viewModel.setTagInput(it) },
                    label = { Text("tag") },
//                    modifier = if (getPlatform().isWeb) Modifier else Modifier.weight(0.3f),
                    modifier = Modifier.weight(0.25f),
                    singleLine = true,
//                    trailingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Clear,
//                            contentDescription = "search",
//                            modifier = Modifier.clickable {
//                                viewModel.setTagInput("")
//                            }
//                        )
//                    }
                )
            }
            //
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                OutlinedTextField(
                    value = highLightInput,
                    onValueChange = { viewModel.setHighLightInput(it) },
                    modifier = Modifier.weight(0.75f),
                    label = { Text("highLight") },
                    singleLine = false,
                    placeholder = {
                        Text(
                            text = "多个参数用后面的分割符区分",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_clear),
                            contentDescription = "clear",
                            modifier = Modifier.size(22.dp).clickable {
                                viewModel.setHighLightInput("")
                            }
                        )
                    },
                )
                OutlinedTextField(
                    value = delimiterInput,
                    onValueChange = viewModel::setDelimiter,
                    modifier = Modifier.weight(0.25f),
                    label = {
                        Text(
                            text = "delimiter",
                            maxLines = 1
                        )
                    },
                )
            }
            //
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Button(onClick = {
                    viewModel.connect(ip, port)
                }) {
                    AppText(
                        text = "connect",
                    )
                }
//                Button(onClick = {
//                    viewModel.close()
//                }) {
//                    AppText(
//                        text = "close"
//                    )
//                }
                Button(onClick = {
                    viewModel.clear()
                }) {
                    Text(
                        text = "clear"
                    )
                }
//                Button(onClick = {
//                    viewModel.addTestMessage()
//                }) {
//                    Text(
//                        text = "test"
//                    )
//                }
            }
            //
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                reverseLayout = false,
            ) {
                items(messageList) {
                    MessageItem(it)
                }
            }
        }
    }
}