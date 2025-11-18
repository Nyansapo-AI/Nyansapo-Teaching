package com.nyansapoai.teaching.presentation.assessments.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.assessments.Assessment
import com.nyansapoai.teaching.utils.Utils.formatDate

@Composable
fun AssessmentItem(
    modifier: Modifier = Modifier,
    assessment: Assessment,
    onClick: () -> Unit = {  }
){
    val iconResource = when (assessment.type) {
        "Numeracy" -> R.drawable.math
        "Literacy" -> R.drawable.literacy
        else -> R.drawable.assessment
    }

    val color = when (assessment.type) {
        "Numeracy" -> Color.Green.copy(alpha = 0.5f)
        "Literacy" -> Color.Yellow.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    ListItem(
        modifier = modifier
            .clip(RoundedCornerShape(5))
            .background(MaterialTheme.colorScheme.tertiary)
            .clickable { onClick.invoke() },
        headlineContent = {
            Text(
                text = assessment.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Bold
            )
        },
        overlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(10))
            ) {
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = when(assessment.type) {
                        "Numeracy" -> "Numeracy Icon"
                        "Literacy" -> "Literacy Icon"
                        else -> "Assessment Icon"
                    },
                    tint = color,
                    modifier = Modifier
                        .size(20.dp)
                )


                Text(
                    text = assessment.type,
                    style = MaterialTheme.typography.titleSmall,
                    color = color,
                    modifier = Modifier
                )
            }
        },
        supportingContent = {
        },
        trailingContent = {
            Text(
                text = when(assessment.assigned_students.size) {
                    0 -> "No students"
                    1 -> "1 student"
                    else -> "${assessment.assigned_students.size} students"
                },
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    )
}


@Composable
fun AssessmentItemBasic(
    modifier: Modifier = Modifier,
    assessment: Assessment,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Image(
            painter = painterResource(
                id = when (assessment.type) {
                    "Numeracy" -> R.drawable.numbers123
                    "Literacy" -> R.drawable.abc
                    else -> R.drawable.abc
                }
            ),
            contentDescription = when (assessment.type) {
                "Numeracy" -> "Numeracy Icon"
                "Literacy" -> "Literacy Icon"
                else -> "Assessment Icon"
            },
            alpha = 0.5f,
            modifier = Modifier
                .padding(16.dp)
                .size(60.dp)
                .align(Alignment.BottomEnd)
        )


        Row(
            modifier = modifier
                .zIndex(1f)
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            )
            {

                // Icon and type
                Text(
                    text = assessment.type,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = when (assessment.type) {
                        "Numeracy" -> Color.Green.copy(alpha = 0.5f)
                        "Literacy" -> Color.Yellow.copy(alpha = 0.5f)
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier
                )


                // Main content

                Text(
                    text = assessment.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = when (assessment.assigned_students.size) {
                        0 -> "No students"
                        1 -> "1 student"
                        else -> "${assessment.assigned_students.size} students"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .zIndex(1f)
                    //                    .align(Alignment.BottomStart)

                )

            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AssessmentItemPreview() {
    AssessmentItemBasic(
        assessment = Assessment(
            id = "1",
            name = "Math Test",
            created_at = "2023-10-01",
            type = "Numeracy",
            start_level = "Beginner",
            assessmentNumber = 1
        )
    )
}