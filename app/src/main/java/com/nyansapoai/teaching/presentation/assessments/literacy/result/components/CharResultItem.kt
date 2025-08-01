package com.nyansapoai.teaching.presentation.assessments.literacy.result.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nyansapoai.teaching.R

@Preview(showBackground = true,)
@Composable
fun CharResultItem(
    modifier: Modifier = Modifier,
    isCorrect: Boolean = false,
    char: String = "a",
    onClick: () -> Unit = {  }
) {

    val color = if (isCorrect) Color(0xFF008000) else Color(0xFFFF0000)
    val image = if (isCorrect) R.drawable.accept else R.drawable.delete_1_

    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Correct Answer",
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .zIndex(1.0f)
        )


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(8.dp)
                .border(
                    width = 4.dp,
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .zIndex(0.8f)
                .clickable(
                    onClick = onClick,
                )
        ) {
            Text(
                text = char,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier
            )
        }
    }
}