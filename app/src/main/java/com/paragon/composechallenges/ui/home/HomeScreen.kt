package com.paragon.composechallenges.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paragon.composechallenges.NavigationItem

@Composable
fun HomeScreen(onNavigate: (NavigationItem) -> Unit) {
    val list = listOf(
        NavigationItem.DynamicViewPager,
        NavigationItem.CarouselViewPager,
        NavigationItem.ViewPagerPagesStateManagement,
        NavigationItem.NestedViewPager,
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize().safeDrawingPadding().padding(vertical = 10.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(list) { _, item ->
            NestedViewPager(onClick = { onNavigate(item) }) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    text = item.route,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun NestedViewPager(
    modifier: Modifier = Modifier, onClick: () -> Unit = {}, content: @Composable () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20))
    ) {
        content()
    }
}