package com.nyansapoai.teaching.presentation.assessments.IndividualAssessment.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.ui.theme.lightPrimary

@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentsStatUI(
    modifier: Modifier = Modifier,
    total: Int = 200,
    completed: Int = 10,
    title: String = "Assessments Progress"
) {

    val balance = total - completed
    val progress: Float =( (total - balance) * 1.0f) / total

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .widthIn(min = 120.dp,)
            .clip(RoundedCornerShape(5))
            .background(MaterialTheme.colorScheme.tertiary)
            .fillMaxWidth()
            .padding(8.dp)
    )
    {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        LinearProgressIndicator(
            progress = {
                progress
            },
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            drawStopIndicator = {
                drawStopIndicator(
                    drawScope = this,
                    stopSize = ProgressIndicatorDefaults.LinearTrackStopIndicatorSize,
                    color = Color.Transparent,
                    strokeCap = StrokeCap.Round
                )
            },
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth(),
            )

        /*
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text(
                text = "Learners",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )


            Text(
                text = total.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            )
        }*/

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text(
                text = "Has done",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = completed.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            )

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Text(
                text = "Has not done",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = balance.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            )

        }


    }
}