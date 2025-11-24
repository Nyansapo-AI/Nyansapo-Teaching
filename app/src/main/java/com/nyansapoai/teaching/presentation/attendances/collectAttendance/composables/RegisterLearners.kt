package com.nyansapoai.teaching.presentation.attendances.collectAttendance.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.students.components.StudentItemUI
import com.nyansapoai.teaching.presentation.students.components.StudentsListUI
import com.nyansapoai.teaching.ui.theme.lightPrimary

@Composable
fun RegisterLearners(
    modifier: Modifier = Modifier,
    studentsList: List<NyansapoStudent> = emptyList(),
    selectedStudents :  List<NyansapoStudent> = emptyList(),
    onSelectStudent: (NyansapoStudent) -> Unit = {  },
    max: Int = 20

) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
        {

            stickyHeader{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = "Register Learners",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        Text(
                            text = "Select the students you want to register",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Left
                        )

                        Text(
                            text = "${selectedStudents.size}/$max",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Right
                        )


                    }

                    Spacer(Modifier.height(12.dp))
                }
            }

            if (studentsList.isEmpty()){
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

            itemsIndexed(items= studentsList){ index,student ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .then(
                            if (student in selectedStudents) Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(lightPrimary)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                            else Modifier
                        )
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                onSelectStudent(student)
                            }
                        )
                        .padding(horizontal = 4.dp, vertical = 12.dp)

                )
                {
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

                        Spacer(Modifier.weight(1f))

                        student.baseline?.let {

                            if (student.baseline.isEmpty()){
                                return@Row
                            }

                            Text(
                                text = student.baseline,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                                    .padding(4.dp)
                            )
                        }

                    }

                }
            }

            item {
                Spacer(Modifier.height(150.dp))
            }
        }


        AppButton(
            onClick = {  },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.8f)
        ){
            Text(
                text = "Submit",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )

        }

    }
}