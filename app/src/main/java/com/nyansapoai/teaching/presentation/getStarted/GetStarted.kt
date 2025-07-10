package com.nyansapoai.teaching.presentation.getStarted

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.navigation.SignInPage
import org.koin.androidx.compose.koinViewModel


@Composable
fun GetStartedRoot() {

    val viewModel = koinViewModel<GetStartedViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    GetStartedScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun GetStartedScreen(
    state: GetStartedState,
    onAction: (GetStartedAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxHeight()
                    .widthIn(max = 500.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.nyansapo_ai_icon_medium),
                    contentDescription = stringResource(R.string.app_icon_description),
                    modifier = Modifier
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {

                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                    ) {

                        Text(
                            text = "Enhance The",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Digital",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Teaching",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Text(
                            text = "Experience",
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )


                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.primary
                            ),
                            onClick = {
                                navController.navigate(SignInPage)
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.arrow_forward),
                                    contentDescription = stringResource(R.string.get_started_action)
                                )

                                Text(
                                    text = stringResource(R.string.get_started),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                            }
                        }
                    }
                }

            }
        }

    }
}