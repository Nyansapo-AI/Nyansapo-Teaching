package com.nyansapoai.teaching.presentation.assessments.createAssessment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.onboarding.components.OptionsItemUI

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentSelectionListUI(
    modifier: Modifier = Modifier,
    optionsList: List<Int?> = listOf(null, 1, 2, 3, 4, 5, 6, 7, 8, 9,) ,
    studentList: List<NyansapoStudent> = emptyList(),
    selectedStudents: List<NyansapoStudent> = emptyList(),
    onSelectStudent: (NyansapoStudent) -> Unit = {  },
    selectedGrade: Int? = null,
    onOptionSelected: (Int?) -> Unit = {  },
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
//            .padding(16.dp)

    ) {
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
            AppDropDownItem(
                item = "${student.first_name} ${student.last_name}",
                isSelected = student in selectedStudents,
                onClick = {
                    onSelectStudent(student)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, )
            )

        }

    }
}