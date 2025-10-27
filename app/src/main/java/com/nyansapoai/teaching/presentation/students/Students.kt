package com.nyansapoai.teaching.presentation.students

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.presentation.students.components.StudentSelectionListUI
import com.nyansapoai.teaching.presentation.students.components.StudentsHeaderUI
import org.koin.androidx.compose.koinViewModel
import java.nio.file.WatchEvent

@Composable
fun StudentsRoot() {

    val viewModel = koinViewModel<StudentsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    StudentsScreen(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun StudentsScreen(
    state: StudentsState,
    onAction: (StudentsAction) -> Unit,
) {

    LaunchedEffect(state.localSchoolInfo, state.selectedGrade) {
        onAction.invoke(
            StudentsAction.OnFetchStudents(
                organizationId = state.localSchoolInfo?.organizationUid ?: "",
                projectId = state.localSchoolInfo?.projectUId ?: "",
                schoolId = state.localSchoolInfo?.schoolUId ?: "",
                grade = state.selectedGrade
            )
        )
    }

    LazyColumn {
        item {
            StudentsHeaderUI(
                studentCount = state.studentList.size
            )
        }



        item {
            StudentSelectionListUI(
                studentList = state.studentList,
                onSelectStudent = { student ->
//                    onAction(StudentsAction.OnSelectStudent(student))
                },
                selectedGrade = state.selectedGrade,
                onOptionSelected = { grade ->
                    onAction(StudentsAction.OnSelectGrade(grade))
                },
                modifier = Modifier
                    .height(600.dp)
            )
        }
    }

}