package com.spacex.launches.launchesList.presentation.model

import com.spacex.launches.core.data.remote.ResponseState

sealed interface LaunchesScreenEffects {
    data class OnError(val error: ResponseState.Error) : LaunchesScreenEffects
}