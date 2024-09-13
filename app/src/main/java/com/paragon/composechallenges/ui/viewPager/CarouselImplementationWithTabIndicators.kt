package com.paragon.composechallenges.ui.viewPager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.paragon.composechallenges.EMPTY
import com.paragon.composechallenges.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarouselImplementationWithTabIndicators() {
    val itemList = listOf(R.mipmap.car8 ,R.mipmap.car9 , R.mipmap.car10 , R.mipmap.car0,R.mipmap.car5, R.mipmap.car1, R.mipmap.car2,R.mipmap.car6, R.mipmap.car3, R.mipmap.car4 , R.mipmap.car7)
    val pagerState = rememberPagerState(pageCount = { itemList.size })
    var targetGradientColors by remember {
        mutableStateOf(
            listOf(
                Color.Blue.copy(.3f),
                Color.Red.copy(.4f)
            )
        )
    }

    val gradientColors by animateColorAsState(
        targetValue = targetGradientColors[0],
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    val gradientColor2 by animateColorAsState(
        targetValue = targetGradientColors[1],
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(pagerState) {
            var currentPageKey by remember { mutableIntStateOf(0) }
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = 2000L)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(page = nextPage)
                    currentPageKey = nextPage
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(gradientColors, gradientColor2)
                )
            ), contentAlignment = Alignment.Center) {
        HorizontalPager(state = pagerState) { page ->
            CarouselItem(itemList[page] , gradientColor = {
                targetGradientColors = it
            })
        }
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .padding(bottom = 100.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            DotIndicators(
                pageCount = pagerState.pageCount,
                pagerState = pagerState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }


}

@Composable
fun CarouselItem(id: Int, gradientColor: (List<Color>) -> Unit = {}) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var colors by remember { mutableStateOf(listOf<Color>()) }

    LaunchedEffect(key1 = id) { // Launch an effect when the ID changes
        withContext(Dispatchers.IO) { // Switch to IO dispatcher for bitmap loading
            bitmap = BitmapFactory.decodeResource(context.resources, id)
        }
        bitmap?.let {
            val palette = Palette.from(it).generate()
            colors = listOf(
                Color(palette.getDominantColor(Color.Red.toArgb())).copy(0.5f),
                Color(palette.getLightVibrantColor(Color.Yellow.toArgb())).copy(0.5f),
                Color(palette.getVibrantColor(Color.Yellow.toArgb())).copy(0.5f)
            )
        }
    }

    // Only call gradientColor when colors are available
    if (colors.isNotEmpty()) {
        gradientColor(colors)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            bitmap?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = EMPTY,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicators(
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.Gray,
    unselectedColor: Color = Color.LightGray,
    pageCount: Int,
    pagerState: PagerState,
) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) selectedColor else unselectedColor
            Box(
                modifier = modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}