package com.nyansapoai.teaching.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.assessments.AssessmentsRoot
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyCountAndMatch
import com.nyansapoai.teaching.presentation.assessments.numeracy.components.NumeracyWordProblem
import com.nyansapoai.teaching.presentation.camps.CampRoot
import com.nyansapoai.teaching.presentation.common.textToSpeech.TextToSpeechRoot
import com.nyansapoai.teaching.presentation.common.audio.AppAudio
import com.nyansapoai.teaching.presentation.assessments.literacy.LiteracyScreen
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyLettersRecognitionUI

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
                screen = { LiteracyLettersRecognitionUI() },
            ),

            BottomNavigationItem(
                title = "Sessions",
                icon = R.drawable.sessions,
                screen = { LiteracyScreen() },
            ),
        )
    }
}
