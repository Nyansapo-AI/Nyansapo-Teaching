package com.nyansapoai.teaching.presentation.assessments.assessmentResult.literacyResult.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nyansapoai.teaching.presentation.assessments.literacy.components.generateComparisonText

@Preview(showBackground = true)
@Composable
fun ParagraphResultItem(
    modifier: Modifier = Modifier,
    expected: String = "Student is writing a paragraph about their favorite animal. The paragraph should include at least three sentences and describe the animal's appearance, behavior, and habitat.",
    studentAnswer: String = "Student is reading a paragraph about their favorite animal.",
    onClick: () -> Unit = {  },
) {

    val comparisonText = generateComparisonText(
        expected = expected,
        actual = studentAnswer
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
//            .padding(16.dp)
            .zIndex(0.8f)
            .clickable(
                onClick = onClick,
            )
    ) {


        Text(
            text = comparisonText,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
        )
    }

}