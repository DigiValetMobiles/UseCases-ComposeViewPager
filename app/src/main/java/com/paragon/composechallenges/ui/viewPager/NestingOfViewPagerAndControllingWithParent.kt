package com.paragon.composechallenges.ui.viewPager

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paragon.composechallenges.EMPTY
import com.paragon.composechallenges.ui.components.Counter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NestingOfViewPagerAndControllingWithParent() {
    val pagerState = rememberPagerState(pageCount = { 10 })

    val scope = rememberCoroutineScope()

    /** state for managing visibility of the child viewPager */
    val childMap = remember { mutableStateMapOf<Int, Int?>() }
    val childScrollTo = remember { mutableStateMapOf<Int , Int?>() }
    val childrenCount = remember { mutableStateMapOf<Int , Int?>() }
    val childCurrentPage = remember { mutableStateMapOf<Int , Int?>() }

    Column {
        HorizontalPager(modifier = Modifier.safeDrawingPadding().wrapContentSize().weight(0.9f), state = pagerState) {
            when (it) {
                0 , 3, 7 , 9  -> {
                    ParentContentItem(
                        currentChild = { child -> childCurrentPage[pagerState.currentPage] = child },
                        scrollToPage = childScrollTo[pagerState.currentPage] ?: 0,
                        childrenCount = childMap[pagerState.currentPage],
                        show = (childMap[pagerState.currentPage] ?: 0) > 0,
                        content = { AddPets(pageName = pagerState.currentPage , onClick = { count -> childrenCount[pagerState.currentPage] = count }) },
                        pageContent = { a ->
                            // showing pager content
                            Box(modifier = Modifier.fillMaxWidth().fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    text = "${it}th parents Child $a"
                                )
                            }
                        })

                }
                else -> {
                    Box(modifier = Modifier.fillMaxWidth().fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            text = "Parent ${ pagerState.currentPage.toString()}"
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            /** on prev. button click of the viewPager */
            Button(onClick = { onPrevClick(pagerState, childrenCount, childScrollTo, childMap, scope) }) {
                Text(text = "Prev")
            }
            /** on next button click of the viewPager */
            Button(onClick = { onNextClick(pagerState , childrenCount , childCurrentPage , childScrollTo , childMap , scope ) }) {
                Text(text = "Next")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun onPrevClick(
    pagerState: PagerState,
    childrenCount: SnapshotStateMap<Int, Int?>,
    childScrollTo: SnapshotStateMap<Int, Int?>,
    childMap: SnapshotStateMap<Int, Int?>,
    scope: CoroutineScope
) {
    val childScrollPosition = childScrollTo[pagerState.currentPage]
    if (childScrollPosition != null) {
        if (childScrollPosition >= 1) childScrollTo[pagerState.currentPage] =
            childScrollTo[pagerState.currentPage]!! - 1
        else if (childScrollPosition == 0) {
            /** reset visibility values here */
            childMap[pagerState.currentPage] = null
            childrenCount[pagerState.currentPage] = null
            childScrollTo[pagerState.currentPage] = null

        } else {
            scope.launch {
                try { pagerState.animateScrollToPage(pagerState.currentPage - 1) } catch (e : Exception) { e.printStackTrace() }
            }
        }
    } else {
        scope.launch {
            try {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun onNextClick(
    pagerState: PagerState,
    childrenCount: SnapshotStateMap<Int, Int?>,
    childCurrentPage: SnapshotStateMap<Int, Int?>,
    childScrollTo: SnapshotStateMap<Int, Int?>,
    childMap: SnapshotStateMap<Int, Int?>,
    scope: CoroutineScope
) {
    val mChildCurrentPage = childCurrentPage[pagerState.currentPage] ?: 0

    if ((childrenCount[pagerState.currentPage] ?: 0) > 0) {
        childMap[pagerState.currentPage] = (childrenCount[pagerState.currentPage] ?: 0)
    }
    if (mChildCurrentPage >= 0 && mChildCurrentPage < (childrenCount[pagerState.currentPage]
            ?: 0) - 1
    ) {
        /** set visibility values here */
        if (childScrollTo[pagerState.currentPage] == null) {
            childScrollTo[pagerState.currentPage] = 0
            childCurrentPage[pagerState.currentPage] = 0
        } else {
            childScrollTo[pagerState.currentPage] =
                childCurrentPage[pagerState.currentPage]?.plus(1)
        }
    } else {
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}

@Preview
@Composable
private fun AddPets(onClick: (Int) -> Unit = {}, pageName: Int = 0) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Would You Like To Register Your Pets for the page $pageName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(20.dp),
            textAlign = TextAlign.Center
        )
        Card(modifier = Modifier.wrapContentSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Icon(
                    modifier = Modifier.padding(20.dp),
                    imageVector = Icons.Default.Pets,
                    contentDescription = EMPTY
                )
                Text(modifier = Modifier.wrapContentWidth(), text = "ADD DOG")
                Counter(onClick = onClick)
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ParentContentItem(
    show: Boolean = false,
    pageContent: @Composable() (PagerScope.(page: Int) -> Unit),
    scrollToPage : Int = -1,
    content: @Composable () -> Unit,
    childrenCount: Int?,
    currentChild :(Int) -> Unit = {}
) {
    val childPager = rememberPagerState { childrenCount ?: 0 }
    LaunchedEffect(scrollToPage) {
        if (scrollToPage >= 0) childPager.animateScrollToPage(scrollToPage)
        if (scrollToPage >= 0) currentChild(scrollToPage)
    }
    AnimatedVisibility(
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }), visible = show) {
        HorizontalPager(modifier = Modifier.fillMaxSize(), state = childPager , pageContent = pageContent)
    }
    AnimatedVisibility(
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
        visible = show.not()) {
        content()
    }
}