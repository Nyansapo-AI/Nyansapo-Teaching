package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoryUI(
    modifier: Modifier = Modifier,
    story: String = "long story text that is used to test the layout of the text and make sure it wraps correctly and does not overflow the bounds of the container. This text is used to ensure that the layout works as expected and that the text is displayed correctly within the bounds of the container.",
    onClose: () -> Unit = { /* Handle close action */ }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Story",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(
                onClick = onClose
            ) {
                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = "Close",
                )
            }
        }

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .padding(16.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.background)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.large
                )
                .padding(16.dp)
        ) {
            Text(
                text = story,
                style = MaterialTheme.typography.titleLarge,

            )
        }
    }
}