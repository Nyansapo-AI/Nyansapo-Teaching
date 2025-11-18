package com.nyansapoai.teaching.presentation.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.school.NyansapoSchool
import com.nyansapoai.teaching.presentation.onboarding.OnboardingSchoolState

@Composable
fun SelectSchool(
    modifier: Modifier = Modifier,
    schoolList: List<NyansapoSchool> = emptyList(),
    selectedSchool: NyansapoSchool? = null,
    onSelectSchool: (NyansapoSchool) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        if (schoolList.isEmpty()){
            Text(
//                text = stringResource(R.string.no_school_linked),
                text = "You are not associated with any village in the project!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth()
            )
            return@Column

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.school),
                contentDescription = "school",
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )

            Text(
//                text = stringResource(R.string.select_school),
                text = "Select the Village",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    schoolList.forEach { school ->

                        OptionsItemUI(
                            text = school.name,
                            isSelected = selectedSchool == school,
                            onClick = {
                                onSelectSchool.invoke(school)
                            }
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(120.dp))
            }
        }

    }

}