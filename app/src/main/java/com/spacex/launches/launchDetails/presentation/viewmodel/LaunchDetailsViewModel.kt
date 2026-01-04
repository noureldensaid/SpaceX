package com.spacex.launches.launchDetails.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.spacex.launches.core.data.remote.ResponseState
import com.spacex.launches.core.navigation.LaunchDetailsScreenRoute
import com.spacex.launches.core.navigation.Navigator
import com.spacex.launches.launchDetails.domain.useCase.GetLaunchDetailsUseCase
import com.spacex.launches.launchDetails.presentation.model.LaunchDetailsEvents
import com.spacex.launches.launchDetails.presentation.model.LaunchDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchDetailsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val getLaunchDetailsUseCase: GetLaunchDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LaunchDetailsState())
    val state = _state.asStateFlow()

    init {
        onEvent(LaunchDetailsEvents.GetLaunchDetails)
    }

    fun onEvent(event: LaunchDetailsEvents) {
        when (event) {
            LaunchDetailsEvents.GetLaunchDetails -> loadLaunchDetails()
            LaunchDetailsEvents.NavigateBack -> navigator.popBackStack()
        }
    }

    private fun loadLaunchDetails() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val launchId = savedStateHandle.toRoute<LaunchDetailsScreenRoute>().id
            launchId?.let {
                val response = getLaunchDetailsUseCase(it)
                when (response) {
                    is ResponseState.Error -> {}
                    is ResponseState.Success -> {
                        val launchDetails = response.data
                        _state.value = _state.value.copy(
                            launchDetails = launchDetails,
                        )
                    }
                }
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}