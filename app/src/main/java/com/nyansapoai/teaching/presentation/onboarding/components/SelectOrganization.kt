package com.nyansapoai.teaching.presentation.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import com.nyansapoai.teaching.R
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectOrganization(
    modifier: Modifier = Modifier,
    organizationList: List<String> = emptyList(),
    onSelectOrganization: (String) -> Unit = {}
){
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.organization1),
                contentDescription = "Organization",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )

            Text(
                text = stringResource(R.string.select_organization),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSelectOrganization(){
    SelectOrganization()
}