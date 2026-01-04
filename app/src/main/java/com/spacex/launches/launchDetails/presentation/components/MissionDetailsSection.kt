package com.spacex.launches.launchDetails.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.spacex.launches.R

@Composable
fun MissionDetailsSection(
    modifier: Modifier = Modifier,
    missionName: String,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.mission),
            fontSize = 22.sp,
            fontWeight = Bold,
        )
        Text(
            text = stringResource(R.string.name, missionName),
            fontSize = 14.sp,
        )
    }
}


@Preview
@Composable
private fun MissionDetailsSectionPreview() {
    MissionDetailsSection(
        missionName = "Starlink Mission",
    )
}