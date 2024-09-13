import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paragon.composechallenges.ui.components.OnLifecycleEvent
import com.paragon.composechallenges.ui.home.NestedViewPager
import com.paragon.composechallenges.ui.viewPager.CommonVIewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPagerPagesStateManagement() {
    val pagerState = rememberPagerState(pageCount = { 1000 })
    var inputText by remember { mutableStateOf("0") }
    val scope = rememberCoroutineScope()

    Column {
        HorizontalPager(modifier = Modifier.safeDrawingPadding().weight(.9f), state = pagerState) {
            when (it) {
                0 -> BasicForm()
                1 -> RandomList()
                2 -> NumberInputView()
                else -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        text = "Parent ${pagerState.currentPage}"
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(modifier = Modifier.weight(0.5f),
                value = inputText,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { inputText = it },
                label = { Text("Enter a number") })

            Button(modifier = Modifier.weight(0.4f), onClick = {
                val number = inputText.toIntOrNull()
                try {
                    scope.launch {
                        if (number != null) {
                            pagerState.animateScrollToPage(number)
                        }
                    }
                } catch (_: Exception) {
                }
            }) {
                Text(text = "SCROLL TO", modifier = Modifier.padding(vertical = 10.dp))
            }
        }

    }
}

@Preview
@Composable
fun BasicForm(vm: CommonVIewModel = viewModel()) {
    val user = vm.user.collectAsState()

    OnLifecycleEvent { _, event ->
        Log.e("TAG", "BasicFormLifeCycle: $event")
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
            }

            else -> Unit
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = user.value?.name.orEmpty(),
            onValueChange = { vm.setName(it) },
            label = { Text("Name") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = user.value?.email.orEmpty(),
            onValueChange = { vm.setEmail(it) },
            label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = user.value?.phone.orEmpty(),
            onValueChange = { vm.setPhone(it) },
            label = { Text("Phone") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /*TODO*/ }) {
            Text("Submit")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RandomList() {
    val randomNumbers by remember {
        mutableStateOf((1..500).map { it })
    }
    OnLifecycleEvent { _, event ->
        Log.e("TAG", "BasicFormLifeCycle Of RandomList: $event")
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
            }

            else -> Unit
        }
    }
    LazyColumn(
        modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(randomNumbers) { number ->
            NestedViewPager(content = {
                Text(modifier = Modifier.padding(20.dp), text = "Item: $number")
            })
        }
    }
}


@Composable
fun NumberInputView() {
    var inputText by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(modifier = Modifier.weight(0.5f),
            value = inputText,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { inputText = it },
            label = { Text("Enter a number") })

        Button(modifier = Modifier.weight(0.4f), onClick = {
            val number = inputText.toIntOrNull()
        }) {
            Text(text = "SCROLL TO", modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}