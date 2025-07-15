package com.nyansapoai.teaching.presentation.students.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.utils.Utils

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentsHeaderUI(
    modifier: Modifier = Modifier,
    studentCount: Int = 123
) {

    var isViewed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit, studentCount) {
        isViewed = true
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .widthIn(max = 600.dp)
            .padding(8.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(16.dp),

        ) {
            Column {
                Text(
                    text = "Total Students",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = modifier,
                )

                Text(
                    text = Utils.animatedNumberString(number = studentCount.toString(), animatedPlayed = isViewed),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier,
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.animated_female_student),
            contentDescription = "Students Header Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
//                .size(200.dp)
                .width(180.dp)
                .align(Alignment.BottomEnd)
        )
    }
}