package com.nyansapoai.teaching.presentation.attendances

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.navigation.CollectAttendancePage
import com.nyansapoai.teaching.presentation.attendances.composables.AttendanceRecordSummary
import com.nyansapoai.teaching.presentation.attendances.composables.DateAttendanceRecord
import com.nyansapoai.teaching.presentation.common.components.AppButton
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

@OptIn(ExperimentalMaterial3Api::class)
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


    LaunchedEffect(state) {
        Log.d("AttendancesScreen", "state: $state")
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

        AnimatedVisibility(
            visible = state.showDetailedAttendance
        ) {
            ModalBottomSheet(
                onDismissRequest = {
                    onAction(AttendancesAction.SetShowDetailedAttendance(showDetailAttendance = false))
                },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),
            ) {
                state.attendanceRecord?.let { attendanceRecord ->
                    DateAttendanceRecord(
                        attendanceRecord = attendanceRecord,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }

        }


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
                },
                selectedWeekDay = state.currentWeekDay
            )


            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = isToday
            ) { isToday ->
               when(isToday){
                   true -> {
                       state.attendanceRecord?.let {

                           AttendanceRecordSummary(
                               attendanceRecord = it,
                               modifier = Modifier
                                   .clickable(
                                       onClick = {
                                           onAction(AttendancesAction.SetShowDetailedAttendance(showDetailAttendance = true))
                                       }
                                   )
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
                                   .clickable(
                                       onClick = {
                                           onAction(AttendancesAction.SetShowDetailedAttendance(showDetailAttendance = true))
                                       }
                                   )
                           )
                       } ?: Column {
                           Text(
                               text = state.currentWeekDay ?: "No selected date",
                               style = MaterialTheme.typography.titleMedium,
                               fontWeight = FontWeight.SemiBold,
                           )


                           Text(
                               text = "No attendance record found",
                               style = MaterialTheme.typography.titleMedium,
                               textAlign = TextAlign.Center,
                               fontWeight = FontWeight.Normal,
                               modifier = Modifier
                                   .padding(12.dp)
                                   .fillMaxWidth()
                           )
                       }
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
    selectedWeekDay: String?
){
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val startDate = remember { currentMonth.minusMonths(0).atStartOfMonth() }
    val endDate = remember { currentDate.plusDays(0) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.W700,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        },
        dayContent = { weekDay ->
            WeekDayItem(
                weekday = weekDay,
                isToday = weekDay.date == currentDate,
                isSelected = selectedWeekDay == weekDay.date.toString() ,
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
    isToday: Boolean,
    isSelected: Boolean,
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
            .background(if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
            .then(if (isSelected) Modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)) else Modifier)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
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