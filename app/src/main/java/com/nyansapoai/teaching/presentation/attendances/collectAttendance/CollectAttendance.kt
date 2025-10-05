package com.nyansapoai.teaching.presentation.attendances.collectAttendance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.students.components.StudentSelectionListUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun CollectAttendanceRoot(
    date: String,
) {

    val viewModel = koinViewModel<CollectAttendanceViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectAttendanceScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CollectAttendanceScreen(
    state: CollectAttendanceState,
    onAction: (CollectAttendanceAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ) { innerPadding ->
        CollectAttendanceContent(
            modifier = Modifier
                .padding(innerPadding)
        )
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectAttendanceContent(
    modifier: Modifier = Modifier,
) {
    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
        )
        {

            Text(
                text = "Student Attendance",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )

            StudentSelectionListUI(
                studentList = List(20) {
                    NyansapoStudent(
                        id = it.toString(),
                        first_name = "FirstName$it",
                        last_name = "LastName$it",
                    )
                },
                studentItemContent = { student ->
                    StudentAttendanceItem(
                        student = student,
                        isSelected = false
                    )

                }
            )

            Spacer(modifier = Modifier.height(70.dp))


        }


        AppButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
//                .padding(16.dp)
        ) {
            Text(
                text = "Submit Attendance",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@Composable
fun StudentAttendanceItem(
    student: NyansapoStudent,
    isSelected: Boolean,
    onClick: (NyansapoStudent) -> Unit = { }
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick(student) }
            )
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = student.first_name + " " + student.last_name,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
        )

        RadioButton(
            selected = isSelected,
            onClick = { onClick(student) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.secondary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier

        )
    }
}