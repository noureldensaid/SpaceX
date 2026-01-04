package com.spacex.launches.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.spacex.launches.core.presentation.theme.SpaceXTheme
import com.spacex.launches.launchesList.presentation.screen.LaunchesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceXTheme {
                LaunchesScreen()
            }
        }
    }
}