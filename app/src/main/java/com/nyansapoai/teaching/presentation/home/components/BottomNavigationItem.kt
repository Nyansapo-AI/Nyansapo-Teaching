package com.nyansapoai.teaching.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.assessments.AssessmentsRoot
import com.nyansapoai.teaching.presentation.assessments.literacy.result.components.ParagraphResultItem
import com.nyansapoai.teaching.presentation.schools.CampRoot
import com.nyansapoai.teaching.presentation.common.components.AppComingSoon
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
                screen = { AppComingSoon() },
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
