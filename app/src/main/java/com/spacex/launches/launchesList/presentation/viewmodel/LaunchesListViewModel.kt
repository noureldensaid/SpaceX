package com.spacex.launches.launchesList.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.core.navigation.LaunchDetailsScreenRoute
import com.spacex.launches.core.navigation.Navigator
import com.spacex.launches.core.utils.Paginator
import com.spacex.launches.launchesList.domain.useCase.GetLaunchesUseCase
import com.spacex.launches.launchesList.presentation.model.LaunchesScreenEvents
import com.spacex.launches.launchesList.presentation.model.LaunchesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LaunchesListViewModel @Inject constructor(
    private val navigator: Navigator,
    private val getLaunchesUseCase: GetLaunchesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LaunchesScreenState())
    val state = _state.asStateFlow()

    private val paginator = Paginator(
        initialLoadSize = 12,
        pageSize = 12,
        initialLoad = { size ->
            getLaunchesUseCase(cursor = null, pageSize = size)
        },
        loadMore = { cursor, size ->
            getLaunchesUseCase(cursor = cursor, pageSize = size)
        },
        getItems = { page -> page.launches },
        getNextCursor = { page -> page.cursor },
        getHasMore = { page -> page.hasMore },
        isEndReached = { endReached ->
            _state.update { it.copy(endReached = endReached) }
        },
        distinctByKey = { it.id }
    )

    init {
        viewModelScope.launch {
            paginator.items.collect { result ->
                when (result) {
                    is ResponseState.Success -> {
                        _state.update {
                            it.copy(
                                launches = result.data,
                                errorMessage = null
                            )
                        }
                    }

                    is ResponseState.Error -> {
                        _state.update {
                            it.copy(
                                errorMessage = result.errorBody?.message ?: "Something went wrong"
                            )
                        }
                    }
                }
            }
        }

        onEvent(LaunchesScreenEvents.LoadLaunches)
    }

    fun onEvent(event: LaunchesScreenEvents) {
        when (event) {
            LaunchesScreenEvents.LoadLaunches -> loadInitial()
            LaunchesScreenEvents.LoadNextPage -> loadNext()
            is LaunchesScreenEvents.NavigateToDetails -> navigateToDetails(event.id)
        }
    }

    private fun loadInitial() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            paginator.loadInitial(force = true)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun loadNext() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true, errorMessage = null) }
            paginator.loadNextPage()
            _state.update { it.copy(isLoadingMore = false) }
        }
    }

    private fun navigateToDetails(launchId: String) {
        navigator.navigate(LaunchDetailsScreenRoute(launchId))
    }
}