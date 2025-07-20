package com.kroy.sseditor.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kroy.ssediotor.BuildConfig
import com.kroy.ssediotor.R
import com.kroy.sseditor.presentation.theme.Primary


@Preview
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp) // Add some padding to avoid edge alignment
        ) {
            Image(
                painterResource(id = R.drawable.logo_circle),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(250.dp, 250.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Display the version name
            Text(
                text = "Version: ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )
        }
    }
}
