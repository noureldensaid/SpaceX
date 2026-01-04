package com.spacex.launches.launchDetails.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.launchDetails.presentation.viewmodel.LaunchDetailsViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun LaunchDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: LaunchDetailsViewModel,
    isLoading: (show: Boolean) -> Unit = {},
    errorFlow: (error: Flow<ResponseState.Error>) -> Unit = {},
    onRetry: (() -> Unit) -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchDetailsScreenRoot(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent
    )

}