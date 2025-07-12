package com.nyansapoai.teaching.presentation.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.organization.NyansapoOrganization
import com.nyansapoai.teaching.presentation.onboarding.OnboardingOrganizationState

@Composable
fun SelectOrganization(
    modifier: Modifier = Modifier,
    organizationList: List<NyansapoOrganization> = emptyList(),
    selectedOrganization: NyansapoOrganization?,
    onSelectOrganization: (NyansapoOrganization) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {

        if (organizationList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_organization_linked),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth()
            )
            return@Column
        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.organization1),
                contentDescription = "organization",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )

            Text(
                text = stringResource(R.string.select_organization),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }



        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            organizationList.forEach { organization ->
                OptionsItemUI(
                    text = organization.name,
                    isSelected = selectedOrganization == organization,
                    onClick = {
                        onSelectOrganization(organization)
                    }
                )
            }
        }
    }
}
/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSelectOrganization(){
    SelectOrganization()
}*/