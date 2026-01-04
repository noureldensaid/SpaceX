package com.spacex.launches.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spacex.launches.core.navigation.LaunchDetailsScreenRoute
import com.spacex.launches.core.navigation.LaunchesScreenRoute
import com.spacex.launches.core.navigation.NavigationEvent
import com.spacex.launches.core.navigation.Navigator
import com.spacex.launches.core.presentation.theme.SpaceXTheme
import com.spacex.launches.core.utils.ObserveAsEvents
import com.spacex.launches.launchDetails.presentation.screen.LaunchDetailsScreen
import com.spacex.launches.launchDetails.presentation.viewmodel.LaunchDetailsViewModel
import com.spacex.launches.launchesList.presentation.screen.LaunchesScreen
import com.spacex.launches.launchesList.presentation.viewmodel.LaunchesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceXTheme {

                val navController = rememberNavController()

                ObserveAsEvents(flow = navigator.navigationEvents.receiveAsFlow()) { navigationEvent ->
                    when (navigationEvent) {
                        is NavigationEvent.Navigate -> navController.navigate(navigationEvent.route) {
                            navigationEvent.popUpToRoute?.let {
                                popUpTo(it) {
                                    inclusive = navigationEvent.inclusive
                                }
                            }
                            launchSingleTop = navigationEvent.isSingleTop
                        }

                        is NavigationEvent.PopBackStack -> {
                            if (navigationEvent.route != null)
                                navController.popBackStack(
                                    navigationEvent.route,
                                    navigationEvent.inclusive
                                )
                            else navController.popBackStack()
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = LaunchesScreenRoute
                ) {
                    composable<LaunchesScreenRoute> {
                        val viewmodel: LaunchesListViewModel = hiltViewModel()
                        LaunchesScreen(viewModel = viewmodel)
                    }
                    composable<LaunchDetailsScreenRoute> {
                        val viewModel: LaunchDetailsViewModel = hiltViewModel()
                        LaunchDetailsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}