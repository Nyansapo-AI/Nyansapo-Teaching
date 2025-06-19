package com.nyansapoai.teaching.presentation.assessments.literacy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.assessments.literacy.components.LiteracyLetterRecognitionUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiteracyRoot() {

    val viewModel = koinViewModel<LiteracyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LiteracyScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LiteracyScreen(
    state: LiteracyState? = null,
    onAction: (LiteracyAction) -> Unit = {},
) {

    var showAppIntro by remember {
        mutableStateOf(true)
    }

    LiteracyLetterRecognitionUI(
        modifier = Modifier
            .padding(16.dp),
    )

}