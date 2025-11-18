package com.nyansapoai.teaching.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.assessments.numeracy.NumeracyOperations
import com.nyansapoai.teaching.presentation.assessments.AssessmentsRoot
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyArithmeticOperationsContainerUI
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyHorizontalArithmeticOperationsUI
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.OperationType
import com.nyansapoai.teaching.presentation.attendances.AttendancesRoot
import com.nyansapoai.teaching.presentation.common.components.AppComingSoon
import com.nyansapoai.teaching.presentation.schools.CampRoot
import com.nyansapoai.teaching.presentation.students.StudentsRoot

data class BottomNavigationItem(
    val title: String,
    @DrawableRes val icon: Int,
    val screen: @Composable () -> Unit,
) {
    companion object {
        val appBottomNavItems = listOf(
            BottomNavigationItem(
                title = "Home",
                icon = R.drawable.home,
                screen = { CampRoot() },
            ),

            BottomNavigationItem(
                title = "Students",
                icon = R.drawable.students,
                screen = { StudentsRoot() },
            ),

            BottomNavigationItem(
                title = "Assessments",
                icon = R.drawable.assessment,
                screen = { AssessmentsRoot() },
            ),

            BottomNavigationItem(
                title = "Attendance",
                icon = R.drawable.attendance,
                screen = {
                    AttendancesRoot()
                },
            ),
            BottomNavigationItem(
                title = "Sessions",
                icon = R.drawable.sessions,
                screen = {
                    AppComingSoon()
                },
            ),
        )
    }
}
