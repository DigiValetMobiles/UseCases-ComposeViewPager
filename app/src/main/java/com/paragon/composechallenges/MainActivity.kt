package com.paragon.composechallenges

import ViewPagerPagesStateManagement
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paragon.composechallenges.ui.home.HomeScreen
import com.paragon.composechallenges.ui.theme.ComposeChallengesTheme
import com.paragon.composechallenges.ui.viewPager.CarouselImplementationWithTabIndicators
import com.paragon.composechallenges.ui.viewPager.DynamicallyAddRemovePagesInViewPager
import com.paragon.composechallenges.ui.viewPager.NestingOfViewPagerAndControllingWithParent

sealed class NavigationItem(var route: String) {
    data object HomeScreen : NavigationItem("HomeScreen")
    data object NestedViewPager : NavigationItem("NestedViewPager")
    data object DynamicViewPager : NavigationItem("DynamicViewPager")
    data object CarouselViewPager : NavigationItem("CarouselViewPager")
    data object ViewPagerPagesStateManagement : NavigationItem("ViewPagerPagesStateManagement")
}


val EMPTY = ""

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ComposeChallengesTheme {
                Scaffold(contentWindowInsets = WindowInsets(0, 0, 0, 0), modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.fillMaxSize().padding(0.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        NavHost(
                            navController = navController,
                            startDestination = NavigationItem.HomeScreen.route
                        ) {
                            composable(NavigationItem.HomeScreen.route) {
                                HomeScreen {
                                    navController.navigate(it.route)
                                }
                            }
                            composable(NavigationItem.NestedViewPager.route) {
                                NestingOfViewPagerAndControllingWithParent()
                            }
                            composable(NavigationItem.DynamicViewPager.route) {
                                DynamicallyAddRemovePagesInViewPager()
                            }
                            composable(NavigationItem.CarouselViewPager.route) {
                                CarouselImplementationWithTabIndicators()
                            }
                            composable(NavigationItem.ViewPagerPagesStateManagement.route) {
                                ViewPagerPagesStateManagement()
                            }
                        }
                    }
                }
            }
        }
    }
}

