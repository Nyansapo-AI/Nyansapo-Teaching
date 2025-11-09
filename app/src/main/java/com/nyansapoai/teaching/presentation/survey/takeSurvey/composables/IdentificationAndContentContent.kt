package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.presentation.common.components.AppCheckBox
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.common.components.AppDropDownMenu
import com.nyansapoai.teaching.presentation.common.components.AppTextField
import com.nyansapoai.teaching.presentation.survey.takeSurvey.County

@Composable
fun IdentificationAndContentContent(
    modifier: Modifier = Modifier,
    name: String,
    nameError: String?,
    county: String,
    subCounty: String,
    ward: String,
    countyList: List<County>,
    showCountyDropdown: Boolean,
    onShowCountyDropdownChanged: (Boolean) -> Unit,
    showSubCountyDropdown: Boolean,
    onShowSubCountyDropdownChanged: (Boolean) -> Unit,
    consentGiven: Boolean?,
    onNameChanged: (String) -> Unit,
    onCountyChanged: (String) -> Unit,
    onSubCountyChanged: (String) -> Unit,
    onWardChanged: (String) -> Unit,
    onConsentChanged: (Boolean) -> Unit
) {

    var selectedCounty by remember(county, countyList) {
        mutableStateOf(countyList.find { it.title == county })
    }

    Column(
        modifier = modifier
    ) {
        AppTextField(
            required = true,
            label = "Full Name",
            value = name,
            error = nameError,
            onValueChanged = onNameChanged,
            placeholder = "Enter your full name",
        )

        /*
        AppDropDownMenu(
            required = true,
            expanded = showCountyDropdown,
            label = "County",
            placeholder = "Select your county",
            onClick = { onShowCountyDropdownChanged(!showCountyDropdown) },
            value = county
        ) {
            countyList.forEach {
                AppDropDownItem(
                    item = it.title,
                    isSelected = it == selectedCounty,
                    onClick = {
                        selectedCounty = countyList.find { it.title == county }
                        onCountyChanged(it.title)
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = selectedCounty != null
        )
        {
            Column {
                AppDropDownMenu(
                    required = true,
                    expanded = showSubCountyDropdown,
                    label = "Sub County",
                    placeholder = "Select your sub county",
                    onClick = { onShowSubCountyDropdownChanged(!showSubCountyDropdown) },
                    value = subCounty
                ) {
                    selectedCounty?.subCounties?.forEach {
                        AppDropDownItem(
                            item = it,
                            isSelected = it == subCounty,
                            onClick = { onSubCountyChanged(it) }
                        )
                    }
                }

                AppTextField(
                    required = true,
                    label = "Ward",
                    value = ward,
                    onValueChanged = onWardChanged,
                    placeholder = "Enter the ward you live in",
                )
            }

        }
        */

        YesNoOption(
            text = " Has the respondent given consent to participate?",
            isYes = consentGiven,
            onChange = onConsentChanged
        )
    }
}