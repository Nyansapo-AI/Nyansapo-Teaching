package com.nyansapoai.teaching.presentation.attendances

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.navigation.CollectAttendancePage
import com.nyansapoai.teaching.presentation.attendances.composables.AttendanceRecordSummary
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppTextField
import com.nyansapoai.teaching.utils.Utils
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun AttendancesRoot() {

    val viewModel = koinViewModel<AttendancesViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AttendancesScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AttendancesScreen(
    state: AttendancesState,
    onAction: (AttendancesAction) -> Unit,
) {

    val selectedDate = state.currentWeekDay

    val isToday = remember(selectedDate) {
        try {
            selectedDate != null &&
                    selectedDate.isNotBlank() &&
                    LocalDate.parse(selectedDate) == LocalDate.now()
        } catch (e: Exception) {
            false
        }
    }

    Scaffold(
        topBar = {
            Text(
                text = stringResource(R.string.attendance),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            WeekContent(
                onSelectedWeekDay = {
                    onAction.invoke(
                        AttendancesAction.SetWeekDay(it)
                    )
                }
            )

            /*
            Text(
                text = state.currentWeekDay ?: "No selected date"
            )

             */

            AnimatedContent(
                targetState = isToday
            ) { isToday ->
               when(isToday){
                   true -> {
                       state.attendanceRecord?.let {
                           AttendanceRecordSummary(
                               attendanceRecord = it,
                               modifier = Modifier
                                   .fillMaxSize()
                           )
                       } ?:
                       Box(
                           modifier = Modifier
                               .fillMaxSize()
                       )
                       {
                           AppButton(
                               onClick = {
                                   Log.d("Attendance Data", "attendance school info: ${state.localSchoolInfo}")

                                   navController.navigate(
                                       CollectAttendancePage(
                                           date = state.currentWeekDay ?: "",
                                           schoolId = state.localSchoolInfo?.schoolUId ?: "",
                                           organizationId = state.localSchoolInfo?.organizationUid ?: "",
                                           projectId = state.localSchoolInfo?.projectUId ?: ""
                                       )
                                   )

                               },
                               modifier = Modifier
                                   .align(Alignment.TopCenter)
                                   .padding(40.dp)
                           ) {
                               Text(
                                   text = stringResource(R.string.take_attendance),
                                   style = MaterialTheme.typography.bodyLarge,
                                   fontWeight = FontWeight.Bold,
                               )
                           }
                       }

                   }
                   false -> {
                       state.attendanceRecord?.let {
                           AttendanceRecordSummary(
                               attendanceRecord = it,
                               modifier = Modifier
                                   .fillMaxSize()
                           )
                       } ?: Text(
                           text = "No attendance record found",
                           style = MaterialTheme.typography.titleLarge,
                           fontWeight = FontWeight.Bold,
                       )
                   }
               }
            }


        }
    }


}


@Composable
fun WeekContent(
    modifier: Modifier = Modifier,
    onSelectedWeekDay: (WeekDay) -> Unit = {},
){
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val startDate = remember { currentMonth.minusMonths(1).atStartOfMonth() } // Adjust as needed
    val endDate = remember { currentDate.plusDays(3) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = firstDayOfWeek
    )

    WeekCalendar(
        state = state,
        weekHeader = { week ->
            Text(
                text = Utils.formatMonthYear(week.days.first().date),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        },
        dayContent = { weekDay ->
            WeekDayItem(
                weekday = weekDay,
                isSelected = weekDay.date == currentDate,
                onClick = {
                    onSelectedWeekDay(it)
                }
            )
        },
        modifier = modifier
    )
}


@Composable
fun WeekDayItem(
    modifier: Modifier = Modifier,
    weekday: WeekDay,
    isSelected: Boolean ,
    onClick: (WeekDay) -> Unit = {}
)
{

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .padding(2.dp)
            .width(72.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = { onClick(weekday) }
            )
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            Text(
                text = weekday.date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )

            Text(
                text = weekday.date.dayOfMonth.toString(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )

            if(weekday.date == LocalDate.now()){
                HorizontalDivider(
                    thickness = 6.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .width(20.dp)
                )
            }
        }

    }
}