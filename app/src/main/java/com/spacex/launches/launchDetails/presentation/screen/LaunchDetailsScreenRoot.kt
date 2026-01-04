package com.spacex.launches.launchDetails.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.spacex.launches.R
import com.spacex.launches.launchDetails.domain.model.LaunchDetailsModel
import com.spacex.launches.launchDetails.presentation.model.LaunchDetailsEvents
import com.spacex.launches.launchDetails.presentation.model.LaunchDetailsState

@Composable
fun LaunchDetailsScreenRoot(
    modifier: Modifier = Modifier,
    state: LaunchDetailsState,
    onEvent: (LaunchDetailsEvents) -> Unit
) {

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                model = state.launchDetails?.missionPatchUrl,
                fallback = painterResource(R.drawable.ic_rocket),
                error = painterResource(R.drawable.ic_rocket),
                contentDescription = state.launchDetails?.missionName,
            )
            Column {
                Text(
                    text = "Roket",
                    fontSize = 22.sp,
                    fontWeight = Bold,
                )
                Text(
                    text = "name:" + state.launchDetails?.rocketName.orEmpty(),
                    fontSize = 14.sp,
                )
                Text(
                    text = "type: " + state.launchDetails?.rocketType.orEmpty(),
                    fontSize = 14.sp,
                )
                Text(
                    text = "id" + state.launchDetails?.rocketId.orEmpty(),
                    fontSize = 14.sp,
                )
            }

            Column {
                Text(
                    text = "Mission",
                    fontSize = 22.sp,
                    fontWeight = Bold,
                )
                Text(
                    text = "name:" + state.launchDetails?.missionName.orEmpty(),
                    fontSize = 14.sp,
                )
            }

            Column {
                Text(
                    text = "Site",
                    fontSize = 22.sp,
                    fontWeight = Bold,
                )
                Text(
                    text = "name:" + state.launchDetails?.site.orEmpty(),
                    fontSize = 14.sp,
                )
            }

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