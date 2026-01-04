package com.spacex.launches.launchesList.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchesList.presentation.model.LaunchesScreenEvents
import com.spacex.launches.launchesList.presentation.viewmodel.LaunchesListViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun LaunchesScreen(
    modifier: Modifier = Modifier,
    viewModel: LaunchesListViewModel,
    isLoading: (show: Boolean) -> Unit = {},
    errorFlow: (error: Flow<ResponseState.Error>) -> Unit = {},
    onRetry: (() -> Unit) -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    isLoading(state.isLoading)

    errorFlow(viewModel.errorFlow)

    onRetry { viewModel.onEvent(LaunchesScreenEvents.LoadLaunches) }

    LaunchesScreenRoot(
        state = state,
        modifier = modifier,
        onEvent = viewModel::onEvent
    )

}