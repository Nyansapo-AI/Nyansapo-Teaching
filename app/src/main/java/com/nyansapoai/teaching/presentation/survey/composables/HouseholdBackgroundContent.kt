package com.nyansapoai.teaching.presentation.survey.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.nyansapoai.teaching.presentation.common.components.AppCheckBox
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.common.components.AppDropDownMenu
import com.nyansapoai.teaching.presentation.common.components.AppTextField

@Composable
fun HouseholdBackgroundContent(
    modifier: Modifier = Modifier,
    respondentName: String,
    onRespondentNameChanged: (String) -> Unit,
    isRespondentHead: Boolean,
    onRespondentHeadChanged: (Boolean) -> Unit,
    householdHeadName: String,
    onHouseholdHeadNameChanged: (String) -> Unit,
    showRelationshipDropdown: Boolean,
    onRelationshipDropdownChanged: (Boolean) -> Unit,
    relationship: String,
    onRelationshipChanged: (String) -> Unit,
    householdHeadMobileNumber: String,
    mobileNumberError: String?,
    onHouseholdHeadMobileNumberChanged: (String) -> Unit,
    respondentAge: String,
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
    hasElectricity: Boolean,
    onHasElectricityChanged: (Boolean) -> Unit
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

    val householdAssets = remember {
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
            "Other"
        )
    }


    Column(
        modifier = modifier
            .imePadding()
    ) {

        AppCheckBox(
            text = "Is the respondent the household head",
            checked = isRespondentHead,
            onCheckedChange = onRespondentHeadChanged
        )

        AppTextField(
            label = "Respondent Name",
            value = respondentName,
            onValueChanged = onRespondentNameChanged,
            placeholder = "Enter the county you live in",
        )

        AppTextField(
            label = "Respondent Age",
            value = respondentAge,
            onValueChanged = {onRespondentAgeChanged(it)},
            keyboardType = KeyboardType.Number,
            placeholder = "Enter the household head name",
        )


        AnimatedVisibility(
            visible = !isRespondentHead
        ) {

            Column {
                AppTextField(
                    label = "Household Head Name",
                    value = householdHeadName,
                    onValueChanged = onHouseholdHeadNameChanged,
                    placeholder = "Enter the household head name",
                )
                AppDropDownMenu(
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
            label = "Household Head Mobile Number",
            value = householdHeadMobileNumber,
            onValueChanged = {onHouseholdHeadMobileNumberChanged(it)},
            keyboardType = KeyboardType.Phone,
            placeholder = "Enter the household head name",
            error = mobileNumberError
        )

        AppDropDownMenu(
            expanded = showMainLanguageDropdown,
            label = "Main Language Spoken at Home",
            placeholder = "Select the main language spoken at home",
            onClick = { onMainLanguageDropdownChanged(!showMainLanguageDropdown)},
            value = mainLanguageSpokenAtHome
        )
        {
            AppDropDownItem(
                item = "English",
                isSelected = mainLanguageSpokenAtHome == "English",
                onClick = {onMainLanguageSpokenAtHomeChanged("English")}
            )
            AppDropDownItem(
                item = "Kiswahili",
                isSelected = mainLanguageSpokenAtHome == "Kiswahili",
                onClick = {onMainLanguageSpokenAtHomeChanged("Kiswahili")}
            )
            AppDropDownItem(
                item = "Mother Tongue",
                isSelected = mainLanguageSpokenAtHome == "Mother Tongue",
                onClick = {onMainLanguageSpokenAtHomeChanged("Mother Tongue")}
            )
            AppDropDownItem(
                item = "Other",
                isSelected = mainLanguageSpokenAtHome == "Other",
                onClick = {onMainLanguageSpokenAtHomeChanged("Other")}
            )

        }


        AppTextField(
            label = "Total Household Members",
            value = householdMembersTotalNumber,
            onValueChanged = {onHouseholdMembersNumberChanged(it)},
            keyboardType = KeyboardType.Number,
            placeholder = "Number of members regularly living in the household including yourself",
            error = null
        )

        AppDropDownMenu(
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

        /*
        AppDropDownMenu(
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
        }*/

        AppCheckBox(
            text = "Does the household have electricity?",
            checked = hasElectricity,
            onCheckedChange = {onHasElectricityChanged(it)}
        )

        AppDropDownMenu(
            expanded = showAssetsDropdown,
            label = "Household Assets",
            placeholder = "Select the main source of income for the household",
            onClick = { onShowAssetsDropdownChanged(!showAssetsDropdown)},
            value = householdAssets.joinToString(", ")
        )
        {
            householdIncomeSources.forEach { source ->
                AppDropDownItem(
                    item = source,
                    isSelected = source in householdAssets,
                    onClick = {onHouseholdAssetsChanged(source)}
                )
            }
        }



    }
}