package com.nyansapoai.teaching.presentation.assessments.assessmentResult

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.assessments.literacy.result.LiteracyResultRoot
import com.nyansapoai.teaching.presentation.assessments.numeracy.results.NumeracyAssessmentResultRoot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentResultRoot(
    assessmentId: String,
    studentId: String,
    studentName: String,
    level: String,
    grade: Int,
    assessmentName: String,
    assessmentType: String,
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        scrolledContainerColor = Color.Transparent,
                        containerColor = Color.Transparent,
                    ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }

                },
                title = {
                    AssessmentResultTopBarTitle(
                        studentName = studentName,
                        grade = grade,
                        level = level,
                        assessmentName = assessmentName,
                        isExpanded = scrollBehavior.state.collapsedFraction > 0,
                        assessmentType = assessmentType
                    )
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        AnimatedContent(
            targetState = assessmentType,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) { type ->
            when(type){
                "Literacy" -> {
                    LiteracyResultRoot(
                        assessmentId = assessmentId,
                        studentId = studentId,
                    )
                }
                "Numeracy" -> {
                    NumeracyAssessmentResultRoot(
                        assessmentId = assessmentId,
                        studentId = studentId,
                    )
                }
                else -> {
                    // Handle unknown assessment type if necessary
                }
            }

        }
    }


}


@Composable
fun AssessmentResultTopBarTitle(
    modifier: Modifier = Modifier,
    studentName: String,
    grade: Int,
    level: String,
    assessmentName: String,
    assessmentType: String,
    isExpanded: Boolean = false,
){

    if(isExpanded) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = studentName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
            )

            Text(
                text = "Grade : $grade",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                modifier = Modifier
            )

        }
    }else {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            Text(
                text = studentName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = assessmentName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    modifier = Modifier
                )

                Text(
                    text = " -  $assessmentType",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }


            Text(
                text = "Grade : $grade",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                modifier = Modifier
            )

            /*
            Text(
                text = level,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                modifier = Modifier
            )*/




        }
    }



}