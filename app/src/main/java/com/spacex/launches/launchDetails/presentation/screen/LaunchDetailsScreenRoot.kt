package com.spacex.launches.launchDetails.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.spacex.launches.R
import com.spacex.launches.launchDetails.domain.model.LaunchDetailsModel
import com.spacex.launches.launchDetails.presentation.components.MissionDetailsSection
import com.spacex.launches.launchDetails.presentation.components.RocketDetailsSection
import com.spacex.launches.launchDetails.presentation.components.SiteDetailsSection
import com.spacex.launches.launchDetails.presentation.model.LaunchDetailsEvents
import com.spacex.launches.launchDetails.presentation.model.LaunchDetailsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailsScreenRoot(
    modifier: Modifier = Modifier,
    state: LaunchDetailsState,
    onEvent: (LaunchDetailsEvents) -> Unit
) {

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Launche ID: #${state.launchDetails?.id}", fontSize = 24.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor =  MaterialTheme.colorScheme.surface,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(LaunchDetailsEvents.NavigateBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 400.dp)
                    .fillMaxHeight(0.5f),
                model = state.launchDetails?.missionPatchUrl,
                fallback = painterResource(R.drawable.ic_rocket),
                error = painterResource(R.drawable.ic_rocket),
                contentDescription = state.launchDetails?.missionName,
            )
            RocketDetailsSection(
                rocketName = state.launchDetails?.rocketName.orEmpty(),
                rocketType = state.launchDetails?.rocketType.orEmpty(),
                rocketId = state.launchDetails?.rocketId.orEmpty(),
            )

            MissionDetailsSection(missionName = state.launchDetails?.missionName.orEmpty())

            SiteDetailsSection(siteName = state.launchDetails?.site.orEmpty())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchDetailsScreenPreview() {
    val mockState = LaunchDetailsState(
        launchDetails = LaunchDetailsModel(
            id = "101",
            missionName = "Starlink-1 (Mock)",
            missionPatchUrl = "https://images2.imgbox.com/d2/3b/bQaWi0m0_o.png",
            rocketName = "Falcon 9",
            site = "adolescens",
            rocketId = "patrioque",
            rocketType = "suscipiantur",

            )
    )

    LaunchDetailsScreenRoot(
        state = mockState,
        onEvent = {}
    )
}