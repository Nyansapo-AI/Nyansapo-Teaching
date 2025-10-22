package com.nyansapoai.teaching.presentation.survey.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nyansapoai.teaching.presentation.common.components.AppCheckBox
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.common.components.AppDropDownMenu
import com.nyansapoai.teaching.presentation.common.components.AppTextField
import com.nyansapoai.teaching.presentation.survey.County

@Composable
fun IdentificationAndContentContent(
    modifier: Modifier = Modifier,
    name: String,
    county: String,
    subCounty: String,
    ward: String,
    countyList: List<County>,
    showCountyDropdown: Boolean,
    onShowCountyDropdownChanged: (Boolean) -> Unit,
    showSubCountyDropdown: Boolean,
    onShowSubCountyDropdownChanged: (Boolean) -> Unit,
    consentGiven: Boolean,
    onNameChanged: (String) -> Unit,
    onCountyChanged: (String) -> Unit,
    onSubCountyChanged: (String) -> Unit,
    onWardChanged: (String) -> Unit,
    onConsentChanged: (Boolean) -> Unit
) {

    var selectedCounty by remember {
        mutableStateOf(countyList.find { it.title == county })
    }

    LaunchedEffect(selectedCounty) {
        Log.d("TAG", "IdentificationAndContentContent: $selectedCounty")
        onSubCountyChanged("")
    }


    Column(
        modifier = modifier
            .imePadding()
    ) {
        AppTextField(
            label = "Full Name",
            value = name,
            onValueChanged = onNameChanged,
            placeholder = "Enter your full name",
        )

        AppDropDownMenu(
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
        ) {
            Column {
                AppDropDownMenu(
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
                    label = "Ward",
                    value = ward,
                    onValueChanged = onWardChanged,
                    placeholder = "Enter the ward you live in",
                )
            }

        }

        AppCheckBox(
            text = "I consent to participate in this survey",
            checked = consentGiven,
            onCheckedChange = onConsentChanged
        )
    }
}