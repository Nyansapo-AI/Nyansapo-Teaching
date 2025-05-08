package com.nyansapoai.teaching.presentation.authentication.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppTextField
import com.nyansapoai.teaching.presentation.navigation.OTPPage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInRoot() {

    val viewModel = koinViewModel<SignInViewModel>()

    val name by viewModel.name.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val canSubmit by viewModel.canSubmit.collectAsState()

    SignInScreen(
        name = name ,
        phoneNumber = phoneNumber,
        canSubmit = canSubmit,
        onAction = viewModel::onAction
    )
}

@Composable
fun SignInScreen(
    name: String,
    phoneNumber: String,
    canSubmit: Boolean,
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
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxHeight()
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .imePadding()
                ) {

                    AppTextField(
                        value = name,
                        placeholder = "Name",
                        imeAction = ImeAction.Next,
                        onValueChanged = { string ->
                            onAction.invoke(SignInAction.OnNameChange(name = string))
                        }
                    )

                    AppTextField(
                        value = phoneNumber,
                        placeholder = "Phone Number",
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number,
                        onValueChanged = { string ->
                            onAction.invoke(SignInAction.OnPhoneNumberChange(phoneNumber = string))
                        }
                    )

                    AppButton(
                        enabled = canSubmit,
                        onClick = {
                            onAction.invoke(SignInAction.OnSubmit(onSuccess = {
                                navController.navigate(OTPPage)
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


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                    ) {
                        Text(
                            text = stringResource(R.string.no_account),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                        )

                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = stringResource(R.string.contact_us),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier
                            )

                        }

                    }
                }




                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {



                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                    ) {
                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = stringResource(R.string.term_services),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                ),
                                modifier = Modifier
                            )

                        }

                        Text(
                            text = stringResource(R.string.and),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                        )

                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = stringResource(R.string.privacy_policy),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                ),
                                modifier = Modifier
                            )

                        }

                    }

                }
            }
        }
    }

}

