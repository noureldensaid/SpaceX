package com.spacex.launches.core.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spacex.launches.R

@Composable
fun LoadingFooter(
    modifier: Modifier = Modifier
) {
    // Use animateFloatAsState to animate the rotation continuously
    val rotation by rememberInfiniteRotation()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_language),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier
                .size(16.dp)
                .graphicsLayer(rotationZ = rotation)
        )
        Text(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 4.dp),
            color = Color.LightGray,
            text = "Loading..."
        )
    }
}

@Composable
fun EndReachedFooter(
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            color = Color.LightGray,
            text = "End of the list"
        )
    }
}

@Composable
fun rememberInfiniteRotation(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewFooter() {
    EndReachedFooter()
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoading() {
    LoadingFooter()
}