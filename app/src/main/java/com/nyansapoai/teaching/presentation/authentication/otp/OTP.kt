package com.nyansapoai.teaching.presentation.authentication.otp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.authentication.otp.components.OTPImplementation
import com.nyansapoai.teaching.presentation.authentication.otp.components.PhoneAuth
import com.nyansapoai.teaching.presentation.common.components.CodeTextField
import com.nyansapoai.teaching.presentation.navigation.OnboardingPage
import com.nyansapoai.teaching.utils.Utils
import org.koin.androidx.compose.koinViewModel

@Composable
fun OTPRoot(
    phoneNumber: String
) {

    val viewModel = koinViewModel<OTPViewModel>()
    val otpCode by viewModel.otpCode.collectAsState()
    val resendTimer by viewModel.timer.collectAsStateWithLifecycle()
    val isOtpComplete by viewModel.isOTPComplete.collectAsState()
    val canSubmit by viewModel.canSubmit.collectAsState()
    val message by viewModel.message.collectAsState()
    val canResendOTPRequest by viewModel.canResendOTPRequest.collectAsState()


    OTPScreen(
        onAction = viewModel::onAction,
        otpCode = otpCode,
        resendTimer = resendTimer,
        canResendOTPRequest = canResendOTPRequest,
        isOTPComplete = isOtpComplete,
        canSubmit = canSubmit,
        phoneNumber = phoneNumber
    )
}

@Composable
fun OTPScreen(
    phoneNumber: String,
    otpCode: String,
    resendTimer: Int,
    canResendOTPRequest: Boolean,
    isOTPComplete: Boolean,
    canSubmit: Boolean = false,
    onAction: (OTPAction) -> Unit,
) {

    Scaffold(
        modifier = Modifier
    ) { innerPadding ->


        AnimatedVisibility(
            visible = true
        ) {
            OTPImplementation(
                phoneNumber = phoneNumber,
                code = otpCode,
                canVerify = canSubmit,
                onVerificationCompleted = {
                    navController.navigate(OnboardingPage)
                },
                onVerificationFailed = {

                }
            )

            /*
            phoneAuth.StartPhoneNumberVerification(
                phoneNumber = phoneNumber,
                code = "343433",
                canVerify = canSubmit,
            )*/
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp),
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .imePadding()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier

                ) {
                    Text(
                        text = stringResource(R.string.otp_title),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    CodeTextField(
                        value = otpCode,
                        obscureText = false,
                        enabled = true,
                        length = 6 ,
                        boxSize = 52.dp,
                        onValueChange = { string ->
                            onAction.invoke(OTPAction.OnOTPCOdeChange(code = string))
                        },
                        modifier = Modifier
                    )

                    Text(
                        text = "Remaining time: ${Utils.secondsToTimerString(seconds = resendTimer)}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Light
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.otp_not_received),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextButton(
                        enabled = canResendOTPRequest,
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.3f
                            )
                        ),
                        onClick = {}
                    ) {
                        Text(
                            text = stringResource(R.string.resend_otp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier
                        )

                    }


                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Button(
                        enabled = isOTPComplete,
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.background
                        ),
                        onClick = {
                            onAction.invoke(OTPAction.OnCanSubmitChange(true))

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.continue_text),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    OutlinedButton(
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),
                        border = BorderStroke(
                            1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                        ),
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)

                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
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