package com.nyansapoai.teaching.presentation.authentication.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.AppButton
import com.nyansapoai.teaching.presentation.common.AppTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInRoot() {

    val viewModel = koinViewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignInScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    state: SignInState,
    onAction: (SignInAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .widthIn(max = 500.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "NYANSAPO",
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        ),

                    )


                    Text(
                        text = "AI",
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        ),


                    )

                }


                Column {
                    Text(
                        text = stringResource(R.string.sign_in),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(R.string.sign_in_desc),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .imePadding()
                ) {

                    AppTextField(
                        value = state.name,
                        placeholder = "Name",
                        onValueChanged = { string ->
                            onAction.invoke(SignInAction.OnNameChange(name = string))
                        }
                    )

                    AppTextField(
                        value = state.phoneNumber,
                        placeholder = "Phone Number",
                        onValueChanged = { string ->
                            onAction.invoke(SignInAction.OnPhoneNumberChange(phoneNumber = string))
                        }
                    )

                    AppButton(
                        enabled = state.submitEnabled,
                        onClick = {
                            onAction.invoke(SignInAction.OnSubmit(onSuccess = {

                            }))
                        },
                        modifier = Modifier
                            .fillMaxWidth(),

                    ) {
                        Text(
                            text = stringResource(R.string.continue_text),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }

}

