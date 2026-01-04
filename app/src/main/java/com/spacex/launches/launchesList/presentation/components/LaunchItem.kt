package com.spacex.launches.launchesList.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.spacex.launches.R
import com.spacex.launches.launchesList.domain.model.Launch

@Composable
fun LaunchItem(
    modifier: Modifier = Modifier,
    launchModel: Launch
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = launchModel.missionPatch,
                contentDescription = launchModel.missionName,
                fallback = painterResource(R.drawable.ic_rocket),
                error = painterResource(R.drawable.ic_rocket),
                modifier = Modifier.fillMaxHeight().weight(0.2f)
            )
            Column(
                modifier = Modifier.weight(0.8f, fill = true),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    color = Color.Black,
                    text = launchModel.rocketName,
                    fontSize = 16.sp,
                    fontWeight = Bold
                )
                Text(
                    text = launchModel.missionName,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Preview()
@Composable
fun LaunchItemPreview() {
    val mockLaunch = Launch(
        rocketName = "Falcon 9",
        missionName = "Starlink",
        missionPatch = "",
        id = "luptatum",
        site = "mauris",
    )
    LaunchItem(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        launchModel = mockLaunch
    )
}