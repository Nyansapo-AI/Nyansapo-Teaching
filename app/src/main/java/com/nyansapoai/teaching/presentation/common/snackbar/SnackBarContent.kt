package com.nyansapoai.teaching.presentation.common.snackbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R

@Composable
fun SnackBarContent(
    modifier: Modifier = Modifier,
    description: String = "",
    snackBarHostState: SnackbarHostState,
    snackBarItem: SnackBarItem?,
    alignBottom: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(description),
        contentAlignment = if (alignBottom) Alignment.BottomCenter else Alignment.TopCenter
    ) {
        SnackbarHost(
            hostState = snackBarHostState,
            snackbar = {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable {
                            snackBarHostState.currentSnackbarData?.dismiss()
                        },
                    border = BorderStroke(
                        3.dp,
                        if (snackBarItem?.isError == true) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary
                    ),
                    colors = CardDefaults.cardColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        IconButton(
                            onClick = {
                                snackBarHostState.currentSnackbarData?.dismiss()
                            },
                            modifier = Modifier
                        ) {
                            Icon(
                                painter = painterResource(if (snackBarItem?.isError == true) R.drawable.warning else R.drawable.check_circle),
                                tint = if (snackBarItem?.isError == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
                                contentDescription = null
                            )
                        }


                        Text(
                            modifier = Modifier
                                .fillMaxWidth(.85f),
                            text = it.visuals.message,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
