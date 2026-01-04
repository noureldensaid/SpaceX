package com.spacex.launches.core.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spacex.launches.core.data.remote.NetworkError
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.core.navigation.LaunchDetailsScreenRoute
import com.spacex.launches.core.navigation.LaunchesScreenRoute
import com.spacex.launches.core.navigation.NavigationEvent
import com.spacex.launches.core.navigation.Navigator
import com.spacex.launches.core.presentation.components.DefaultConnectionError
import com.spacex.launches.core.presentation.components.DefaultLoadingComponent
import com.spacex.launches.core.presentation.theme.SpaceXTheme
import com.spacex.launches.core.utils.ObserveAsEvents
import com.spacex.launches.launchDetails.presentation.screen.LaunchDetailsScreen
import com.spacex.launches.launchDetails.presentation.viewmodel.LaunchDetailsViewModel
import com.spacex.launches.launchesList.presentation.screen.LaunchesScreen
import com.spacex.launches.launchesList.presentation.viewmodel.LaunchesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.flowOf
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

                var isLoading by remember { mutableStateOf(false) }

                var errorFlow by remember { mutableStateOf(flowOf<ResponseState.Error>()) }

                var onRetry: () -> Unit by remember { mutableStateOf({}) }

                var isNetworkConnectionError by remember { mutableStateOf(false) }

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

                ObserveAsEvents(flow = errorFlow) { error ->
                    when (error.error) {
                        NetworkError.NO_INTERNET_CONNECTION -> isNetworkConnectionError = true
                        else -> Toast.makeText(this, error.errorBody?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                Box(Modifier.fillMaxSize()) {
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(if (isLoading) Modifier.blur(2.dp) else Modifier),
                    navController = navController,
                    startDestination = LaunchesScreenRoute,
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(300)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(300)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(300)
                        )
                    }
                ) {
                    composable<LaunchesScreenRoute> {
                        val viewmodel: LaunchesListViewModel = hiltViewModel()
                        LaunchesScreen(
                            viewModel = viewmodel,
                            isLoading = { isLoading = it },
                            errorFlow = { errorFlow = it },
                            onRetry = { onRetry = it }
                        )
                    }
                    composable<LaunchDetailsScreenRoute> {
                        val viewModel: LaunchDetailsViewModel = hiltViewModel()
                        LaunchDetailsScreen(
                            viewModel = viewModel,
                            isLoading = { isLoading = it },
                            errorFlow = { errorFlow = it },
                            onRetry = { onRetry = it }
                        )
                    }
                }
                    DefaultLoadingComponent(isVisible = isLoading)

                    DefaultConnectionError(
                        isVisible = isNetworkConnectionError,
                    ) {
                        onRetry()
                        isNetworkConnectionError = false

                    }
                }
            }
        }
    }
}