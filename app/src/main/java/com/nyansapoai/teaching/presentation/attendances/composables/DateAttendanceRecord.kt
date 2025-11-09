package com.nyansapoai.teaching.presentation.attendances.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.domain.models.attendance.AttendanceRecord
import com.nyansapoai.teaching.domain.models.attendance.StudentAttendance
import com.nyansapoai.teaching.ui.theme.lightPrimary

@Composable
fun DateAttendanceRecord(
    modifier: Modifier = Modifier,
    attendanceRecord: AttendanceRecord,
)
{
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Text(
                text = attendanceRecord.date.ifBlank { "No date" },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (attendanceRecord.students.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No students", style = MaterialTheme.typography.bodyLarge)
                }
                return@Column
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = attendanceRecord.students) { student ->
                    StudentAttendanceRowReadOnly(student = student)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun StudentAttendanceRowReadOnly(
    student: StudentAttendance,
    modifier: Modifier = Modifier
)
{
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)

        ) {
            Text(text = student.name, style = MaterialTheme.typography.bodyLarge)
            student.grade?.let {
                Text(text = "Grade: $it", style = MaterialTheme.typography.bodySmall)
            }
        }

        val statusText = if (student.attendance) "Present" else "Absent"
        val statusColor = if (student.attendance) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

        Text(
            text = statusText,
            color = statusColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Composable
fun AttendanceRecordSummary(
    attendanceRecord: AttendanceRecord,
    modifier: Modifier = Modifier
) {
    val total = attendanceRecord.students.size
    val present = attendanceRecord.students.count { it.attendance }
    val absent = total - present
    val percent = if (total > 0) (present * 100.0f / total) else 0f
    val absentPreview = attendanceRecord.students
        .filterNot { it.attendance }
        .take(5)
        .joinToString(", ") { it.name }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = lightPrimary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = attendanceRecord.date.ifBlank { "No date" },
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(text = "Total: $total", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Present: $present", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Absent: $absent", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = String.format("%.0f%% present", percent),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            if (absent > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(6.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                    ) {
                        Text(
                            text = "Absent (preview):",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = absentPreview.ifEmpty { "—" },
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        if (absent > 5) {
                            Text(
                                text = "and ${absent - 5} more",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}