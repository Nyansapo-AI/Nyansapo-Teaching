package com.nyansapoai.teaching.presentation.survey.detailedHouseHold.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.Parent
import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.ui.theme.lightPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdDetailScreen(
    household: HouseHoldInfo
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        household.householdHeadName ?: "Household Details",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Back"
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                InfoCard {
                    if (household.intervieweeName.isNotBlank()){
                        KeyValueRow("Interviewer", household.intervieweeName)
                    }
                    KeyValueRow("Interview Date", household.interviewDate)
                    KeyValueRow("Interviewee", household.intervieweeName)
//                    KeyValueRow("County", household.county)
//                    KeyValueRow("Sub-county", household.subCounty)
//                    KeyValueRow("Ward", household.ward)
                    KeyValueRow("Consent given", household.consentGiven.toString())
                    KeyValueRow("Household members", household.householdMembersCount?.toString() ?: "N/A")
                    KeyValueRow("Income source", household.incomeSource ?: "N/A")
                    KeyValueRow("Has electricity", household.hasElectricity?.toString() ?: "N/A")
                }
            }

            item {
                InfoCard {
                    KeyValueRow("Respondent", household.respondentName ?: "N/A")
                    KeyValueRow("Respondent age", household.respondentAge?.toString() ?: "N/A")
                    KeyValueRow("Household head", household.householdHeadName ?: "N/A")
                    KeyValueRow("Relationship to head", household.relationshipToHead ?: "N/A")
                    KeyValueRow("Head phone", household.householdHeadPhone ?: "N/A")
                    KeyValueRow("Main language", household.mainLanguage ?: "N/A")
                    KeyValueRow("Marital status", household.maritalStatus ?: "N/A")
                }
            }

            if (household.householdAssets.isNotEmpty()) {
                item {
                    InfoCard {
                        Text("Assets", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 6.dp))
                        household.householdAssets.forEach { asset ->
                            KeyValueRow("•", asset)
                        }
                    }
                }
            }

            if (household.children.isNotEmpty()) {
                item {
                    Text("Children", style = MaterialTheme.typography.titleMedium)
                }
                items(household.children) { child ->
                    ChildItem(child)
                }
            }

            if (household.parents.isNotEmpty()) {
                item {
                    Text("Parents", style = MaterialTheme.typography.titleMedium)
                }
                items(household.parents) { parent ->
                    ParentItem(parent)
                }
            }

            item {
                Column {
                    Divider()
                    Spacer(Modifier.height(12.dp))

                    household.parentalEngagement?.let { parentalEngagement ->
                        Text(
                            "Parental Engagement",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        InfoCard {
                            /*
                            KeyValueRow(
                                "Has school age child",
                                parentalEngagement.hasSchoolAgeChild.toString()
                            )*/
                            KeyValueRow(
                                "Homework helper",
                                parentalEngagement.homeworkHelper ?: "N/A"
                            )
                            KeyValueRow(
                                "Teacher discussion freq.",
                                parentalEngagement.teacherDiscussionFrequency ?: "N/A"
                            )
                            KeyValueRow(
                                "Attends meetings",
                                parentalEngagement.attendsSchoolMeetings?.toString() ?: "N/A"
                            )
                            KeyValueRow(
                                "Monitors attendance",
                                parentalEngagement.monitorsAttendance?.toString() ?: "N/A"
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))



                    household.childLearningEnvironment?.let { learningEnvironment ->
                        Text(
                            "Learning Environment",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        InfoCard {

                            KeyValueRow(
                                "Quiet place to study",
                                learningEnvironment.hasQuietPlaceToStudy.toString()
                            )
                            KeyValueRow(
                                "Has books/materials",
                                learningEnvironment.hasBooksOrMaterials.toString()
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Divider()

                }
            }
        }
    }
}

@Composable
private fun InfoCard(content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = lightPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(12.dp),
        )
        {
            content()
        }
    }
}

@Composable
private fun KeyValueRow(key: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(key, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ChildItem(child: Child) {
    InfoCard {
        KeyValueRow("Name", child.firstName)
        KeyValueRow("Gender", child.gender)
        KeyValueRow("Age", child.age)
        KeyValueRow("Grade", child.grade)
        KeyValueRow("Lives with", child.livesWith)
        KeyValueRow("Was assessed in 2024", child.wasAssessedIn2024.toString())
        if (child.wasAssessedIn2024){
            KeyValueRow("Reached Story Level in 2024", child.wasAboveStoryLevelIn2024.toString())
        }
    }
}

@Composable
private fun ParentItem(parent: Parent) {
    InfoCard {
        KeyValueRow("Name", parent.name)
        KeyValueRow("Age", parent.age.toString())
        KeyValueRow("Type", parent.type)
        KeyValueRow("Attended school", parent.hasAttendedSchool.toString())
        KeyValueRow("Highest education", parent.highestEducationLevel)
    }
}