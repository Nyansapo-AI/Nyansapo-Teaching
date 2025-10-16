package com.nyansapoai.teaching.presentation.assessments.assessmentResult.literacyResult.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nyansapoai.teaching.R

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharResultItem(
    modifier: Modifier = Modifier,
    isCorrect: Boolean = true,
    char: String = "paragraph",
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
                .size(24.dp)
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .zIndex(1.0f)
        )


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(6.dp)
                .border(
                    width = 2.dp,
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical =6.dp, horizontal = 12.dp)
                .zIndex(0.8f)
                .clickable(
                    onClick = onClick,
                )
        ) {
            Text(
                text = char,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
            )
        }
    }
}