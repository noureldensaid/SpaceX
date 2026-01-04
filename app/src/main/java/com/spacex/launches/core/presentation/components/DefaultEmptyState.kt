package com.spacex.launches.core.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacex.launches.R

@Composable
fun DefaultEmptyState(
    modifier: Modifier = Modifier,
    @StringRes body: Int? = null,
    @StringRes title: Int? = null,
    painter: Painter = painterResource(id = R.drawable.ic_connection_error),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .padding(14.dp),
            painter = painter,
            contentDescription = null
        )

        title?.let {
            Text(
                text = stringResource(id = it),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        body?.let {
            Text(
                text = stringResource(id = it),
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmptyState() {
    DefaultEmptyState(
        title = R.string.no_avaliable_data,
        body = R.string.no_avaliable_data,
        painter = painterResource(id = R.drawable.ic_connection_error),
    )
}
