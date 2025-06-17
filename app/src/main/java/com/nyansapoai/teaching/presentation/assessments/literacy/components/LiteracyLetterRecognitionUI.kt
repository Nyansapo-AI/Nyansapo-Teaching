package com.nyansapoai.teaching.presentation.assessments.literacy.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle
import com.nyansapoai.teaching.R
import io.ktor.client.plugins.HttpRequestRetryEvent


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LiteracyLetterRecognitionUI(
    modifier: Modifier = Modifier,

) {
    var showAppIntro by remember {
        mutableStateOf(true)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
        modifier = modifier
            .fillMaxSize()
    ){

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Letter Recognition",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    onClick = {}
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "click for instructions"
                        )

                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                TextButton(
                    onClick = {}
                ) {
                    Text(
                        text = "End",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                    )
                }
            }

        }


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .heightIn(min = 200.dp, max = 300.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(5))
                .background(MaterialTheme.colorScheme.tertiary)
        ){
            Text(
                text = "A",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onTertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 160.sp,
            )
        }

        IntroShowcase(
            showIntroShowCase = showAppIntro,
            dismissOnClickOutside = false,
            onShowCaseCompleted = {
                //App Intro finished!!
                showAppIntro = false
            }
        ) {

            /*
            FloatingActionButton(
                onClick = {},
                modifier = Modifier.introShowCaseTarget(
                    index = 0,
                    style = ShowcaseStyle.Default.copy(
                        backgroundColor = Color(0xFF1C0A00), // specify color of background
                        backgroundAlpha = 0.98f, // specify transparency of background
                        targetCircleColor = Color.White // specify color of target circle
                    ),
                    // specify the content to show to introduce app feature
                    content = {
                        Column {
                            Text(
                                text = "Check emails",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Click here to check/send emails",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Icon(
                                painterResource(id = R.drawable.arrow_back),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.End),
                                tint = Color.White
                            )
                        }
                    }
                ),
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = "Email"
                )
            }*/

            IconButton (
                onClick = {},
                modifier = Modifier
                    .introShowCaseTarget(
                        index = 0,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color(0xFF1C0A00),
                            backgroundAlpha = 0.8f,
                            targetCircleColor = Color.White
                        ),
                        content = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Read Letter",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Hold here to read the letter aloud",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.mic),
                    contentDescription = "Hold to speak",
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }

        }

    }

}