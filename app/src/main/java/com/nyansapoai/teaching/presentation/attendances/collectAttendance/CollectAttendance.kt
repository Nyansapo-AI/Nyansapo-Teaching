package com.nyansapoai.teaching.presentation.attendances.collectAttendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.attendance.StudentAttendance
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.components.AppButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectAttendanceScreen(
    state: CollectAttendanceState,
    onAction: (CollectAttendanceAction) -> Unit,
    date: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                title = {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )

        },
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ) { innerPadding ->
        CollectAttendanceContent(
            isLoading = state.isLoading,
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
    isLoading: Boolean = false,
    onSubmitAttendance: () -> Unit = {  },
)
{
    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
        )
        {

            Text(
                text = "Take Attendance",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )

            StudentAttendanceList(
                studentList = studentList,
                onOptionSelected = onOptionSelected,
                selectedGrade = selectedGrade,
                onSelectStudent = onClickStudent
            )

            Spacer(modifier = Modifier.height(70.dp))
        }


        AppButton(
            isLoading = isLoading,
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
    number: Int = 0,
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${number}.",
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = student.name,
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }




        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        )
        {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (student.attendance == true) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = if (student.attendance == true) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onClick(student)
                }
            ) {

                Text(
                    text = "Present",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

            }
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (student.attendance == false) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    containerColor = if (student.attendance == false) MaterialTheme.colorScheme.errorContainer else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onClick(student)
                }
            ) {

                Text(
                    text = "Absent",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

            }
        }
    }
}


@Composable
fun StudentAttendanceList(
    modifier: Modifier = Modifier,
    studentList: List<StudentAttendance> = emptyList(),
    onSelectStudent: (StudentAttendance) -> Unit = {  },
    selectedGrade: Int? = null,
    onOptionSelected: (Int?) -> Unit = {  },
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {

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

        itemsIndexed(items= studentList){ index, student ->
            StudentAttendanceItem(
                number = index + 1,
                student = student,
                isSelected = student.attendance,
                onClick = {
                    onSelectStudent(student)
                },
                modifier = Modifier
            )

        }

        item {
            Spacer(Modifier.height(120.dp))
        }

    }
}