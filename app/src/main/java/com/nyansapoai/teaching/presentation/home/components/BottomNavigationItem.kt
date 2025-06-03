package com.nyansapoai.teaching.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.assessments.AssessmentsRoot
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyCountAndMatch
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.camps.CampRoot
import com.nyansapoai.teaching.presentation.common.components.AppTakeScreenShoot

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
                screen = { NumeracyCountAndMatch() },
            ),

            BottomNavigationItem(
                title = "Sessions",
                icon = R.drawable.sessions,
                screen = { NumeracyWordProblem() },
            ),
        )
    }
}
