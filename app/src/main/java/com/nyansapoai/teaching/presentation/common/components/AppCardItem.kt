package com.nyansapoai.teaching.presentation.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.utils.Utils

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppCardItem(
    modifier: Modifier = Modifier,
    title: String = "Teachers",
    count: String = "12",
    brush: Brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.tertiary,MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.secondary)),
    @DrawableRes imageResId:  Int = R.drawable.animated_teacher
) {

    var isViewed by remember { mutableStateOf(false) }

    LaunchedEffect(true){
        isViewed = true
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(130.dp)
            .width(240.dp)
            .clip(RoundedCornerShape(5))
            .background(brush = brush)
            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
    )
    {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp)
        )
        {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Text(
                text = Utils.animatedNumberString(number = count, animatedPlayed = isViewed),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.ExtraBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground
            )

        }

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = title,
            modifier = Modifier
                .size(120.dp)
        )
    }
}