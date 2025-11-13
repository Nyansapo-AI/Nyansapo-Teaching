package com.nyansapoai.teaching.presentation.survey.takeSurvey

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.presentation.common.components.AppAlertDialog
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.StepContent
import com.nyansapoai.teaching.presentation.common.components.StepsRow
import com.nyansapoai.teaching.presentation.survey.takeSurvey.composables.ChildLearningEnvironmentContent
import com.nyansapoai.teaching.presentation.survey.takeSurvey.composables.FamilyMembersContent
import com.nyansapoai.teaching.presentation.survey.takeSurvey.composables.HouseholdBackgroundContent
import com.nyansapoai.teaching.presentation.survey.takeSurvey.composables.IdentificationAndContentContent
import com.nyansapoai.teaching.presentation.survey.takeSurvey.composables.ParentalEngagementContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun SurveyRoot() {

    val viewModel = koinViewModel<SurveyViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SurveyScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(
    state: SurveyState,
    onAction: (SurveyAction) -> Unit,
) {

    val context = LocalContext.current

    val surveyStepState = rememberLazyListState()

    LaunchedEffect(state.localSchoolInfo, state.isLinkedIdList) {

        state.localSchoolInfo?.let {
            onAction.invoke(
                SurveyAction.FetchAvailableStudents(localSchoolInfo = state.localSchoolInfo)
            )
        }

    }


    LaunchedEffect(state.currentStepIndex) {
        surveyStepState.animateScrollToItem(state.currentStepIndex)
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    BackHandler(enabled = true) {
        if (state.currentStepIndex > 0){
            onAction(SurveyAction.OnUpdateCurrentIndex(state.currentStepIndex -1))
            return@BackHandler
        }

        onAction(SurveyAction.SetShowPrematureExitDialog(show = true))
    }

    val surveySteps = listOf(
        // Define your survey steps here
        StepContent(
            screen = {
                IdentificationAndContentContent(
                    interviewer = state.interviewerName,
                    interviewee = state.intervieweeName,
                    interviewerError = state.interviewerNameError,
                    nameError = state.intervieweeNameError,
                    consentGiven = state.consentGiven,
                    onIntervieweeChanged = { onAction(SurveyAction.SetIntervieweeName(it)) },
                    onInterviewerChanged = {onAction(SurveyAction.SetInterviewerName(it))},
                    onConsentChanged = { onAction(SurveyAction.SetConsentGiven(it)) }
                )
            },
            onSubmit = {
            },
            title = "Identification and Consent"
        ),


        StepContent(
            screen = {
                HouseholdBackgroundContent(
                    respondentName = state.respondentName,
                    respondentNameError = state.respondentNameError,
                    respondentAgeError = state.respondentAgeError,
                    respondentAge = state.respondentAge,
                    onRespondentNameChanged = { onAction(SurveyAction.SetRespondentName(it)) },
                    isRespondentHead = state.isRespondentHeadOfHousehold,
                    onRespondentHeadChanged = {
                        onAction(
                            SurveyAction.SetIsRespondentHeadOfHousehold(
                                it
                            )
                        )
                    },
                    householdHeadName = state.householdHeadName,
                    householdHeadNameError = state.householdHeadNameError,
                    onHouseholdHeadNameChanged = { onAction(SurveyAction.SetHouseholdHeadName(it)) },
                    showRelationshipDropdown = state.showRelationshipToHeadDropdown,
                    onRelationshipDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowRelationshipToHeadDropdown(
                                it
                            )
                        )
                    },
                    relationship = state.relationshipToHead,
                    onRelationshipChanged = { onAction(SurveyAction.SetRelationshipToHead(it)) },
                    householdHeadMobileNumber = state.householdHeadMobileNumber,
                    mobileNumberError = state.mobileNumberError,
                    onHouseholdHeadMobileNumberChanged = {
                        onAction(
                            SurveyAction.SetHouseholdHeadMobileNumber(
                                it
                            )
                        )
                    },
                    onRespondentAgeChanged = { onAction(SurveyAction.SetRespondentAge(it)) },
                    showMainLanguageDropdown = state.showMainLanguageDropdown,
                    onMainLanguageDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowMainLanguageDropdown(
                                it
                            )
                        )
                    },
                    maritalStatus = state.maritalStatus,
                    showMaritalStatusDropdown = state.showMaritalStatusDropdown,
                    onMaritalStatusChanged = { onAction(SurveyAction.SetHeadMaritalStatus(it)) },
                    onShowMaritalStatusDropdownChanged = {onAction(SurveyAction.SetShowMaritalStatusDropdown(it))},
                    mainLanguageSpokenAtHome = state.mainLanguageSpokenAtHome,
                    onMainLanguageSpokenAtHomeChanged = {
                        onAction(
                            SurveyAction.SetMainLanguageSpokenAtHome(
                                it
                            )
                        )
                    },
                    householdMembersTotalNumber = state.totalHouseholdMembers,
                    onHouseholdMembersNumberChanged = {
                        onAction(
                            SurveyAction.SetTotalHouseholdMembers(
                                it
                            )
                        )
                    },
                    houseHoldIncomeSource = state.houseHoldIncomeSource,
                    onHouseHoldIncomeSourceChanged = {
                        onAction(
                            SurveyAction.SetHouseholdIncomeSource(
                                it
                            )
                        )
                    },
                    showIncomeSourceDropdown = state.showIncomeSourceDropdown,
                    onShowIncomeSourceDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowIncomeSourceDropdown(
                                it
                            )
                        )
                    },
                    showAssetsDropdown = state.showAssetsDropdown,
                    onShowAssetsDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowAssetsDropdown(it)
                        )
                    },
                    householdAssets = state.householdAssets,
                    onHouseholdAssetsChanged = {
                        onAction(
                            SurveyAction.SetHouseholdAssetsAddRemove(
                                it
                            )
                        )
                    },
                    hasElectricity = state.hasElectricity,
                    onHasElectricityChanged = { onAction(SurveyAction.SetHasElectricity(it)) },
                )
            },
            onSubmit = {
            },
            title = "Household Background"
        ),

        StepContent(
            screen = {
                FamilyMembersContent(
                    showParentOrGuardianSheet = state.showParentOrGuardianSheet,
                    onShowParentOrGuardianSheetChange = {
                        onAction(
                            SurveyAction.SetShowParentOrGuardianSheet(
                                it
                            )
                        )
                    },
                    parentName = state.parentName,
                    parentNameError = state.parentNameError,
                    onParentNameChanged = { onAction(SurveyAction.SetParentName(it)) },
                    parentAge = state.parentAge,
                    parentAgeError = state.parentAgeError,
                    onParentAgeChanged = { onAction(SurveyAction.SetParentAge(it)) },
                    hasAttendedSchool = state.hasAttendedSchool,
                    onHasAttendedSchoolChanged = { onAction(SurveyAction.SetHasAttendedSchool(it)) },
                    showHigherEducationDropdown = state.showHigherEducationDropdown,
                    onShowHighEducationDropdownChange = {
                        onAction(
                            SurveyAction.SetShowHigherEducationDropdown(
                                it
                            )
                        )
                    },
                    highestEducationLevel = state.highestEducationLevel,
                    onHighestEducationLevelChanged = {
                        onAction(
                            SurveyAction.SetHighestEducationLevel(
                                it
                            )
                        )
                    },
                    parentGender = state.parentGender,
                    onParentGenderChanged = { onAction(SurveyAction.SetParentGender(it)) },
                    showGuardianGenderDropdown = state.showGuardianGenderDropdown,
                    onShowGuardianGenderDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowGuardianGenderDropdown(
                                it
                            )
                        )
                    },
                    type = state.type,
                    onTypeChanged = { onAction(SurveyAction.SetType(it)) },
                    showTypeDropdown = state.showTypeDropdown,
                    onShowTypeDropdownChanged = { onAction(SurveyAction.SetShowTypeDropdown(it)) },
                    onAddParent = { onAction(SurveyAction.OnAddParent) },
                    onRemoveParent = { onAction(SurveyAction.OnRemoveParent(it)) },
                    parents = state.parents,
                    showAddChildSheet = state.showAddChildSheet,
                    onShowAddChildSheetChange = { onAction(SurveyAction.SetShowAddChildSheet(it)) },
                    childFirstName = state.childFirstName,
                    onChildFirstNameChanged = { onAction(SurveyAction.SetChildFirstName(it)) },
                    childLastName = state.childLastName,
                    onChildLastNameChanged = { onAction(SurveyAction.SetChildLastName(it)) },
                    childGender = state.childGender,
                    onChildGenderChanged = { onAction(SurveyAction.SetChildGender(it)) },
                    childWasAssessedIn2024 = state.wasChildAssessedIn2024,
                    onChildWasAssessedIn2024Changed = {onAction(SurveyAction.SetChildWasAssessedIn2024(it) )},
                    childWasLevelAboveStory = state.childWasLevelAboveStory,
                    onChildWasLevelAboveStoryChanged = {onAction(SurveyAction.SetChildWasLevelAboveStoryIn2024(it) )},
                    showChildGenderDropdown = state.showChildGenderDropdown,
                    onShowChildGenderDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowChildGenderDropdown(
                                it
                            )
                        )
                    },
                    showChildGradeDropdown = state.showChildGradeDropdown,
                    onShowChildGradeDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowChildGradeDropdown(
                                it
                            )
                        )
                    },
                    livesWith = state.livesWith,
                    childGrade = state.childGrade,
                    childGradeError = state.childGradeError,
                    onChildGradeChanged = { onAction(SurveyAction.SetChildGrade(it)) },
                    onLivesWithChanged = { onAction(SurveyAction.SetLivesWith(it)) },
                    showLivesWithDropdown = state.showLivesWithDropdown,
                    onShowLivesWithDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowLivesWithDropdown(
                                it
                            )
                        )
                    },
                    onAddChild = { onAction(SurveyAction.OnAddChild) },
                    onRemoveChild = { onAction(SurveyAction.OnRemoveChild(it)) },
                    children = state.children,
                    childAge = state.childAge,
                    childAgeError = state.childAgeError,
                    onChildAgeChanged = { onAction(SurveyAction.SetChildAge(it)) },
                    linkedLearnerId = state.linkedLearnerId,
                    onLinkedLearnerIdChange = { onAction(SurveyAction.SetLinkedLearnerId(it)) },
                    showAvailableLearnersDropdown = state.showAvailableLearnersDropdown,
                    onShowAvailableLearnersDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowAvailableLearnerDropdown(
                                it
                            )
                        )
                    },
                    availableLearners = state.availableLearners,
                    error = state.familyMemberError,
                    modifier = Modifier,

                )
            },
            onSubmit = {},
            title = "Family Members"
        ),

        StepContent(
            screen = {
                ParentalEngagementContent(
                    isSchoolAgePresent = state.isSchoolAgeChildrenPresent,
                    onSchoolAgeChanged = {
                        onAction(
                            SurveyAction.SetIsSchoolAgeChildrenPresent(
                                it
                            )
                        )
                    },
                    showWhoHelpsDropdown = state.showWhoHelpsDropdown,
                    onShowWhoHelpsDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowWhoHelpsDropdown(
                                it
                            )
                        )
                    },
                    whoHelps = state.whoHelps,
                    onWhoHelpsChanged = { onAction(SurveyAction.SetWhoHelps(it)) },
                    showDiscussDropdown = state.showDiscussWithTeachersDropdown,
                    onShowDiscussDropdownChanged = {
                        onAction(
                            SurveyAction.SetShowDiscussWithTeachersDropdown(
                                it
                            )
                        )
                    },
                    discussFrequency = state.discussFrequency,
                    onDiscussFrequencyChanged = {
                        onAction(
                            SurveyAction.SetDiscussFrequency(
                                it
                            )
                        )
                    },
                    attendMeetings = state.doAttendMeetings,
                    onAttendMeetingsChanged = {
                        onAction(
                            SurveyAction.SetDoAttendMeetings(
                                it
                            )
                        )
                    },
                    monitorAttendance = state.doMonitorAttendance,
                    onMonitorAttendanceChanged = {
                        onAction(
                            SurveyAction.SetDoMonitorAttendance(
                                it
                            )
                        )
                    },
                    otherWhoHelps = state.otherWhoHelps,
                    onOtherWhoHelpsChanged = {
                        onAction(
                            SurveyAction.SetOtherWhoHelps(
                                it
                            )
                        )
                    }
                )
            },
            onSubmit = {
            },
            title = "Parental Engagement"
        ),
        StepContent(
            screen = {
                ChildLearningEnvironmentContent(
                    isQuietPlaceAvailable = state.isQuietPlaceAvailable,
                    onQuietPlaceChanged = {
                        onAction(
                            SurveyAction.SetHasQuietPlaceAvailable(
                                it
                            )
                        )
                    },
                    hasLearningMaterials = state.hasLearningMaterials,
                    onHasLearningMaterialsChanged = {
                        onAction(
                            SurveyAction.SetHasLearningMaterials(
                                it
                            )
                        )
                    }
                )
            },
            onSubmit = {
            },
            title = "Child Learning Environment"
        ),

    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                onClick = {

                                    if (state.currentStepIndex > 0){
                                        onAction(SurveyAction.OnUpdateCurrentIndex(state.currentStepIndex -1))
                                        return@IconButton
                                    }

                                    onAction(SurveyAction.SetShowPrematureExitDialog(show = true))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.arrow_back),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    contentDescription = "Back",
                                )
                            }

                            Text(
                                text = "Household Survey",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
//                                .padding(start = 16.dp,)
                            )
                        }

                        StepsRow(
                            state = surveyStepState,
                            numberOfSteps = surveySteps.size,
                            currentStep = state.currentStepIndex,
                            stepDescriptionList = surveySteps,
                            onClick = { step ->

                                if (step <= state.currentStepIndex) {
                                    onAction(SurveyAction.OnUpdateCurrentIndex(step - 1))

                                }
                            },
                            modifier = Modifier
//                                .padding(start = 16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { innerPadding ->


        AnimatedVisibility(
            visible = state.prematureExitDialog,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AppAlertDialog(
                onDismissRequest = { onAction(SurveyAction.SetShowPrematureExitDialog(show = false)) },
                dialogText = "You are about to exit the survey, and you will lose all data. Click confirm to continue.",
                dialogTitle = "Exit Survey",
                onConfirmation = {
                    onAction(SurveyAction.SetShowPrematureExitDialog(show = true))
                    navController.popBackStack()
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .padding(16.dp)
        ){

            item {
                surveySteps[state.currentStepIndex].screen(Modifier.padding(innerPadding))
            }

            item {
                Spacer(Modifier.height(28.dp))
            }

            item {
                AppButton(
                    enabled = state.canSubmit(),
                    isLoading = state.isLoading,
                    onClick = {
                        if (state.currentStepIndex < surveySteps.size -1){
                            onAction(SurveyAction.OnUpdateCurrentIndex(state.currentStepIndex +1))
                        } else {
                            onAction(
                                SurveyAction.SubmitSurvey(
                                    onSuccess = {
                                        navController.popBackStack()
                                    }
                                )
                            )
                        }
                    }
                ) {
                    Text(
                        text = if (state.currentStepIndex == surveySteps.size -1) "Submit" else "Next"
                    )
                }
            }

        }


    }
}