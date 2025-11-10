package com.nyansapoai.teaching.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.assessments.AssessmentsRoot
import com.nyansapoai.teaching.presentation.attendances.AttendancesRoot
import com.nyansapoai.teaching.presentation.schools.CampRoot
import com.nyansapoai.teaching.presentation.students.StudentsRoot
import com.nyansapoai.teaching.presentation.survey.household.HouseholdRoot

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
                title = "Survey",
                icon = R.drawable.sessions,
                screen = {
                    HouseholdRoot()
                },
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

        )
    }
}
