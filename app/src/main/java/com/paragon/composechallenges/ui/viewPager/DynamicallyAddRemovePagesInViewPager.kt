package com.paragon.composechallenges.ui.viewPager

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DynamicallyAddRemovePagesInViewPager() {
    var items by remember { mutableStateOf(listOf("Page 1", "Page 2", "Page 3")) }
    val pagerState = rememberPagerState(pageCount = { items.size })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column {
        HorizontalPager(modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .weight(0.8f) , state = pagerState) { page ->
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.8f)
                    .background(
                        Color(
                            red = Random.nextFloat(),
                            green = Random.nextFloat(),
                            blue = Random.nextFloat(),
                            alpha = 1f
                        )
                    )
            ){
                Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = items[page])
                TextButton(onClick = {
                    val mList = items.toMutableList()
                    mList.removeAt(page)
                    items = mList
                }) {
                    Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "Remove Me")
                }
            }
        }

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
            .weight(0.2f)
            .fillMaxWidth() , verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                items = items + "Page ${items.size + 1}"
                scope.launch {
                    if (items.isNotEmpty() && items.size > 1) pagerState.animateScrollToPage(items.size)
                }

            }) { Text("Add Page") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
              try {
                  val randomIndex = Random.nextInt(items.size)
                  scope.launch { pagerState.animateScrollToPage(randomIndex) }
                  val mList = items.toMutableList()
                  mList.add(randomIndex, "Random $randomIndex")
                  items = mList
                  Toast.makeText(context, "Inserted at Index $randomIndex", Toast.LENGTH_SHORT).show()
              }catch (_:Exception){}
            }) { Text("Add Random") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {items = items.dropLast(1) }) { Text("Remove Last") }
        }
    }
}