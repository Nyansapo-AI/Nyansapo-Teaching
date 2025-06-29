package com.nyansapoai.teaching.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.assessments.AssessmentsRoot
import com.nyansapoai.teaching.presentation.assessments.literacy.components.MultichoiceQuestionItemUI
import com.nyansapoai.teaching.presentation.assessments.literacy.components.MultichoiceQuestionsUI
import com.nyansapoai.teaching.presentation.camps.CampRoot

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
                screen = {  },
            ),

            BottomNavigationItem(
                title = "Assessment",
                icon = R.drawable.assessment,
                screen = { AssessmentsRoot() },
            ),

            BottomNavigationItem(
                title = "Attendance",
                icon = R.drawable.attendance,
                screen = { MultichoiceQuestionItemUI() },
            ),

            BottomNavigationItem(
                title = "Sessions",
                icon = R.drawable.sessions,
                screen = {  },
            ),
        )
    }
}
