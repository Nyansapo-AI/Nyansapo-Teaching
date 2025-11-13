package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.common.components.AppDropDownMenu
import com.nyansapoai.teaching.presentation.common.components.AppTextField

@Composable
fun HouseholdBackgroundContent(
    modifier: Modifier = Modifier,
    respondentName: String,
    respondentNameError: String?,
    onRespondentNameChanged: (String) -> Unit,
    isRespondentHead: Boolean?,
    onRespondentHeadChanged: (Boolean) -> Unit,
    householdHeadName: String,
    householdHeadNameError: String?,
    onHouseholdHeadNameChanged: (String) -> Unit,
    showRelationshipDropdown: Boolean,
    onRelationshipDropdownChanged: (Boolean) -> Unit,
    relationship: String,
    onRelationshipChanged: (String) -> Unit,
    householdHeadMobileNumber: String,
    mobileNumberError: String?,
    onHouseholdHeadMobileNumberChanged: (String) -> Unit,
    respondentAge: String,
    respondentAgeError: String?,
    onRespondentAgeChanged: (String) -> Unit,
    showMainLanguageDropdown: Boolean,
    onMainLanguageDropdownChanged: (Boolean) -> Unit,
    mainLanguageSpokenAtHome: String,
    onMainLanguageSpokenAtHomeChanged: (String) -> Unit,
    householdMembersTotalNumber: String,
    onHouseholdMembersNumberChanged: (String) -> Unit,
    houseHoldIncomeSource: String,
    onHouseHoldIncomeSourceChanged: (String) -> Unit,
    showIncomeSourceDropdown: Boolean,
    onShowIncomeSourceDropdownChanged: (Boolean) -> Unit,
    householdAssets: MutableList<String> = mutableStateListOf(),
    onHouseholdAssetsChanged: (String) -> Unit = {},
    showAssetsDropdown: Boolean,
    onShowAssetsDropdownChanged: (Boolean) -> Unit,
    hasElectricity: Boolean?,
    onHasElectricityChanged: (Boolean) -> Unit,
    maritalStatus: String,
    onMaritalStatusChanged: (String) -> Unit,
    showMaritalStatusDropdown: Boolean,
    onShowMaritalStatusDropdownChanged: (Boolean) -> Unit,
) {

    val householdIncomeSources = remember {
        listOf(
            "Salaried employment",
            "Casual work",
            "Self-employment",
            "Support from others",
            "Other"
        )
    }

    val householdAssetsList = remember {
        listOf(
            "Radio",
            "Television",
            "Mobile phone",
            "Computer/tablet",
            "Motorcycle",
            "Car/Truck",
            "Bicycle"
        )
    }

    val relations = remember {
        listOf(
            "Wife",
            "Husband",
            "Sibling",
            "Parent",
            "Child",
            "Relative",
            "Other"
        )
    }

    val languageSpoken = remember {
        listOf(
            "Kiswahili",
            "Mother Tongue",
            "Other"
        )
    }

    val maritalStatusList = remember {
        listOf(
            "Widowed",
            "Divorced",
            "Separated",
            "Single",
            "Never married",
            "Living together"
        )
    }


    Column(
        modifier = modifier
            .imePadding()
    ) {

        YesNoOption(
            showError = isRespondentHead == null,
            text = "Is the respondent the household head?",
            isYes = isRespondentHead,
            onChange = onRespondentHeadChanged
        )

        AppTextField(
            showError = respondentNameError != null || respondentName.isEmpty(),
            required = true,
            label = "Name of the respondent",
            value = respondentName,
            error = respondentNameError,
            onValueChanged = onRespondentNameChanged,
            placeholder = "Enter the respondent name",
        )

        AppTextField(
            showError = respondentAgeError != null || respondentAge.isEmpty(),
            required = true,
            label = "Respondent Age",
            error = respondentAgeError,
            value = respondentAge,
            onValueChanged = {onRespondentAgeChanged(it)},
            keyboardType = KeyboardType.Number,
            placeholder = "Enter the respondent age",
        )


        AnimatedVisibility(
            visible = isRespondentHead == false
        )
        {

            Column {
                AppTextField(
                    showError = isRespondentHead == false && (householdHeadNameError != null || householdHeadName.isEmpty()),
                    required = true,
                    label = "Household Head Name",
                    value = householdHeadName,
                    error = householdHeadNameError,
                    onValueChanged = onHouseholdHeadNameChanged,
                    placeholder = "Enter the household head name",
                )
                AppDropDownMenu(
                    showError = relationship.isEmpty() || relationship.isBlank(),
                    required = true,
                    expanded = showRelationshipDropdown,
                    label = "Select Relationship to Household Head",
                    placeholder = "How are you related with the household head",
                    onClick = { onRelationshipDropdownChanged(!showRelationshipDropdown)},
                    value = relationship
                )
                {

                    Column {
                        relations.forEach { relation ->
                            AppDropDownItem(
                                item = relation,
                                isSelected = relationship == relation,
                                onClick = {onRelationshipChanged(relation)}
                            )

                        }
                    }
                }
            }
        }



        AppTextField(
            showError = mobileNumberError != null || householdHeadMobileNumber.isEmpty(),
            required = true,
            label = "Telephone/Mobile Number of the Household Head",
            value = householdHeadMobileNumber,
            onValueChanged = {onHouseholdHeadMobileNumberChanged(it)},
            keyboardType = KeyboardType.Phone,
            placeholder = "Enter the household head mobile number",
            error = mobileNumberError
        )

        AppDropDownMenu(
            showError = mainLanguageSpokenAtHome.isEmpty() || mainLanguageSpokenAtHome.isBlank(),
            required = true,
            expanded = showMainLanguageDropdown,
            label = "Main Language Spoken at Home",
            placeholder = "Select the main language spoken at home",
            onClick = { onMainLanguageDropdownChanged(!showMainLanguageDropdown)},
            value = mainLanguageSpokenAtHome
        )
        {

            Column {
                languageSpoken.forEach { language ->
                    AppDropDownItem(
                        item = language,
                        isSelected = mainLanguageSpokenAtHome == language,
                        onClick = {onMainLanguageSpokenAtHomeChanged(language)}
                    )
                }
            }

        }


        AppTextField(
            showError = householdMembersTotalNumber.isEmpty() || householdMembersTotalNumber.isBlank(),
            required = true,
            label = "Number of members regularly living in the household including yourself",
            value = householdMembersTotalNumber,
            onValueChanged = {onHouseholdMembersNumberChanged(it)},
            keyboardType = KeyboardType.Number,
            placeholder = "Number of members regularly living in the household including yourself",
            error = null
        )

        AppDropDownMenu(
            showError = houseHoldIncomeSource.isEmpty() || houseHoldIncomeSource.isBlank(),
            required = true,
            expanded = showIncomeSourceDropdown,
            label = "Main Source of Income",
            placeholder = "Select the main source of income for the household",
            onClick = { onShowIncomeSourceDropdownChanged(!showIncomeSourceDropdown)},
            value = houseHoldIncomeSource
        )
        {
            householdIncomeSources.forEach { source ->
                AppDropDownItem(
                    item = source,
                    isSelected = houseHoldIncomeSource == source,
                    onClick = {onHouseHoldIncomeSourceChanged(source)}
                )
            }
        }

        AppDropDownMenu(
            showError = maritalStatus.isEmpty() || maritalStatus.isBlank(),
            required = true,
            expanded = showMaritalStatusDropdown,
            label = "Household Head Marital status",
            placeholder = "What is the marital status of the household head?",
            onClick = { onShowMaritalStatusDropdownChanged(!showMaritalStatusDropdown)},
            value = maritalStatus,
        ) {
            maritalStatusList.forEach { status ->
                AppDropDownItem(
                    item = status,
                    isSelected = status == maritalStatus,
                    onClick = { onMaritalStatusChanged(status) }
                )
            }
        }

        YesNoOption(
            showError = hasElectricity == null,
            text = "Does the household have electricity?",
            isYes = hasElectricity,
            onChange = {onHasElectricityChanged(it)}
        )

        AppDropDownMenu(
            expanded = showAssetsDropdown,
            label = "Household Assets",
            placeholder = "Select the main source of income for the household",
            onClick = { onShowAssetsDropdownChanged(!showAssetsDropdown)},
            value = householdAssets.joinToString(", ")
        )
        {
            householdAssetsList.forEach { asset ->
                AppDropDownItem(
                    item = asset,
                    isSelected = asset in householdAssets,
                    onClick = {onHouseholdAssetsChanged(asset)}
                )
            }
        }



    }
}