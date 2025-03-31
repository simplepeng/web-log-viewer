package simple.library.weblogviewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MainScreen(
    viewModel: MainViewModel = MainViewModel()
) {

    var ip by remember { mutableStateOf("172.16.1.63") }
    var port by remember { mutableStateOf("8080") }

    val messageList by viewModel.messageList.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("ip") },
                )
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("port") },
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
                Button(onClick = {
                    viewModel.close()
                }) {
                    AppText(
                        text = "close"
                    )
                }
                Button(onClick = {
                    viewModel.clear()
                }) {
                    Text(
                        text = "clear"
                    )
                }
            }
            //
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
            ) {
                items(messageList) {
                    AppText(
                        text = it.text,
                    )
                }
            }
        }
    }
}