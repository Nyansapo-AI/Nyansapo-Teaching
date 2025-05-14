package com.nyansapoai.teaching.presentation.onboarding.components

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.onboarding.OrganizationUI

@Composable
fun SelectOrganization(
    modifier: Modifier = Modifier,
    organizationList: List<OrganizationUI> = emptyList(),
    selectedOrganization: OrganizationUI?,
    onSelectOrganization: (OrganizationUI) -> Unit = {}
){

    LaunchedEffect(true) {
        Log.d("selected organization", "selected UI: $selectedOrganization")
    }

    var selectedItem by remember { mutableStateOf<OrganizationUI?>(selectedOrganization) }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.organization1),
                contentDescription = "Organization",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )

            Text(
                text = stringResource(R.string.select_organization),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }


        if (organizationList.isEmpty()){
            Text(
                text = stringResource(R.string.no_organization_linked),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
            )

            return@Column
        }
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            organizationList.forEach { organization ->
                OptionsItemUI(
                    text = organization.name,
                    isSelected = selectedItem == organization,
                    onClick = {
                        selectedItem = organization
                        onSelectOrganization.invoke(organization)
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