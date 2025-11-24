package com.nyansapoai.teaching.presentation.attendances.collectAttendance.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.domain.models.school.NyansapoSchool
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.presentation.students.components.StudentsListUI

@Composable
fun RegisterLearners(
    modifier: Modifier = Modifier,
    studentsList: List<NyansapoStudent> = emptyList()
) {
    Column() {
        StudentsListUI(
            studentList = studentsList,
            onSelectStudent = {},

        )
    }
}