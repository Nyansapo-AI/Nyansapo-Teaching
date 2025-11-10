package com.nyansapoai.teaching.presentation.students.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.onboarding.components.OptionsItemUI

@Composable
fun StudentSelectionListUI(
    modifier: Modifier = Modifier,
    optionsList: List<Int?> = listOf(null, 1, 2, 3, 4, 5, 6, 7, 8, 9,) ,
    studentList: List<NyansapoStudent> = emptyList(),
    selectedStudents: List<NyansapoStudent> = emptyList(),
    onSelectStudent: (NyansapoStudent) -> Unit = {  },
    studentItemContent: @Composable (NyansapoStudent) -> Unit = { student ->
        AppDropDownItem(
            item = student.name.ifEmpty { "${student.first_name} ${student.last_name}" },
            isSelected = student in selectedStudents,
            onClick = {
                onSelectStudent(student)
            },
            modifier = Modifier
                .padding(horizontal = 4.dp, )
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
                            .then(if (item == optionsList.first()) Modifier.padding(start = 16.dp) else Modifier)
                    )
                }
            }
        }

        if (studentList.isEmpty()){
            item {
                Text(
                    text = "No Learner found",
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



        itemsIndexed(items= studentList){ index,student ->
            studentItemContent(student)
        }

    }
}
@Composable
fun StudentsListUI(
    modifier: Modifier = Modifier,
    optionsList: List<Int?> = listOf(null, 1, 2, 3, 4, 5, 6, 7, 8, 9,) ,
    levelList: List<String?> = listOf(null,"Beginner", "Word", "Paragraph"),
    studentList: List<NyansapoStudent> = emptyList(),
    selectedStudents: List<NyansapoStudent> = emptyList(),
    onSelectStudent: (NyansapoStudent) -> Unit = {  },
    selectedGrade: Int? = null,
    selectedLevel: String? = null,
    onLevelSelected: (String?) -> Unit = {  },
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {

        stickyHeader{
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
//                    .padding(horizontal = 12.dp)
            ) {
                items(levelList) { item ->
                    OptionsItemUI(
                        text = item ?: "All",
                        isSelected = item == selectedLevel,
                        onClick = { onLevelSelected(item) },
                        modifier = Modifier
//                            .then(if( item == levelList.last() )Modifier.padding(end = 16.dp) else Modifier)
//                            .then(if( item == levelList.first() )Modifier.padding(end = 16.dp) else Modifier)
                    )
                }
            }
        }


        if (studentList.isEmpty()){
            item {
                Text(
                    text = "No Learner found",
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



        itemsIndexed(items= studentList){ index,student ->
//            studentItemContent(student)
            StudentItemUI(
                student = student,
                index = index,
                onSelectStudent = onSelectStudent,
            )
        }

    }
}








@Composable
fun StudentItemUI(
    modifier: Modifier = Modifier,
    student: NyansapoStudent,
    index: Int = 0,
    onSelectStudent: (NyansapoStudent) -> Unit = {  },
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onSelectStudent(student) }
            )
            .padding(horizontal = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${index + 1}.",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${student.first_name} ${student.last_name}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )

        }

    }
}