package com.nyansapoai.teaching.presentation.attendances.collectAttendance

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.domain.models.attendance.StudentAttendance
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.onboarding.components.OptionsItemUI
import com.nyansapoai.teaching.presentation.students.components.StudentSelectionListUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun CollectAttendanceRoot(
    date: String,
    schoolId: String,
    organizationId: String,
    projectId: String,
) {

    val viewModel = koinViewModel<CollectAttendanceViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectAttendanceScreen(
        state = state,
        onAction = viewModel::onAction,
        date = date
    )
}

@Composable
fun CollectAttendanceScreen(
    state: CollectAttendanceState,
    onAction: (CollectAttendanceAction) -> Unit,
    date: String
) {
    Scaffold(
        topBar = {
            Text(
                text = date,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        },
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ) { innerPadding ->
        CollectAttendanceContent(
            studentList = state.studentAttendanceList,
            selectedGrade = state.selectedGrade,
            onOptionSelected = { onAction(CollectAttendanceAction.OnSelectedGrade(it)) },
            onClickStudent = { student -> onAction.invoke(CollectAttendanceAction.OnMarkAttendance(studentId = student.id, isPresent = !student.attendance))},
            onSubmitAttendance = {
                onAction.invoke(CollectAttendanceAction.OnSubmitAttendance(
                    onSuccess = {
                        navController.popBackStack()
                    }
                ))
            },
            modifier = Modifier
                .padding(innerPadding)
        )
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CollectAttendanceContent(
    modifier: Modifier = Modifier,
    studentList: List<StudentAttendance> = emptyList(),
    onClickStudent: (StudentAttendance) -> Unit = {  },
    onOptionSelected: (Int?) -> Unit = {  },
    selectedGrade: Int? = null,
    onSubmitAttendance: () -> Unit = {  },
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

            StudentAttendanceList(
                studentList = studentList,
                onOptionSelected = onOptionSelected,
                selectedGrade = selectedGrade ,
                studentItemContent = { student ->
                    StudentAttendanceItem(
                        student = student,
                        isSelected = student.attendance,
                        onClick = { onClickStudent(student) },
                    )
                }
            )

            Spacer(modifier = Modifier.height(70.dp))
        }


        AppButton(
            onClick = onSubmitAttendance,
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
    modifier: Modifier = Modifier,
    student: StudentAttendance,
    isSelected: Boolean,
    onClick: (StudentAttendance) -> Unit = { },
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick(student) }
            )
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = student.name,
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


@Composable
fun StudentAttendanceList(
    modifier: Modifier = Modifier,
    optionsList: List<Int?> = listOf(null, 1, 2, 3, 4, 5, 6, 7, 8, 9,),
    studentList: List<StudentAttendance> = emptyList(),
    onSelectStudent: (StudentAttendance) -> Unit = {  },
    studentItemContent: @Composable (StudentAttendance) -> Unit = { student ->
        StudentAttendanceItem(
            student = student,
            isSelected = student.attendance,
            onClick = {
                onSelectStudent(student)
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, )
        )
    },
    selectedGrade: Int? = null,
    onOptionSelected: (Int?) -> Unit = {  },
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {
        /*
        stickyHeader {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(optionsList) { item ->
                    OptionsItemUI(
                        text = item?.let { "Grade $item" } ?: "All",
                        isSelected = item == selectedGrade,
                        onClick = { onOptionSelected(item) },
                        modifier = Modifier
                            .then(if (item == optionsList.last()) Modifier.padding(end = 16.dp) else Modifier)
                            .then(if(item == optionsList.first()) Modifier.padding(start = 16.dp) else Modifier)
                    )
                }
            }
        }

         */

        if (studentList.isEmpty()){
            item {
                Text(
                    text = "No students found",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            return@LazyColumn
        }

        items(items= studentList){ student ->
            studentItemContent(student)
        }

    }
}