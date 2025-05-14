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
import com.nyansapoai.teaching.presentation.onboarding.OnboardingProjectState

@Composable
fun SelectProject(
    modifier: Modifier = Modifier,
    projectList: List<OnboardingProjectState>,
    selectedProject: OnboardingProjectState?,
    onSelectProject: (OnboardingProjectState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        if (projectList.isEmpty()){
            Text(
                text = stringResource(R.string.no_project_linked),
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
                contentDescription = "project",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )

            Text(
                text = stringResource(R.string.select_project),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            projectList.forEach { project ->

                OptionsItemUI(
                    text = project.name,
                    isSelected = selectedProject == project,
                    onClick = {
                        onSelectProject.invoke(project)
                    }
                )
            }
        }

    }


}