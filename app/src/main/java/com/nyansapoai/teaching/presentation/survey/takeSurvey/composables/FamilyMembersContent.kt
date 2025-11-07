package com.nyansapoai.teaching.presentation.survey.takeSurvey.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.students.NyansapoStudent
import com.nyansapoai.teaching.domain.models.survey.Child
import com.nyansapoai.teaching.domain.models.survey.Parent
import com.nyansapoai.teaching.presentation.common.components.AppButton
import com.nyansapoai.teaching.presentation.common.components.AppCheckBox
import com.nyansapoai.teaching.presentation.common.components.AppDropDownItem
import com.nyansapoai.teaching.presentation.common.components.AppDropDownMenu
import com.nyansapoai.teaching.presentation.common.components.AppTextField
import com.nyansapoai.teaching.presentation.onboarding.components.OptionsItemContent
import com.nyansapoai.teaching.ui.theme.lightPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyMembersContent(
    modifier: Modifier = Modifier,
    showParentOrGuardianSheet: Boolean = false,
    onShowParentOrGuardianSheetChange: (Boolean) -> Unit = {},
    parentName: String = "",
    parentNameError: String?,
    onParentNameChanged: (String) -> Unit = {},
    parentAge: String = "",
    parentAgeError: String?,
    onParentAgeChanged: (String) -> Unit = {},
    hasAttendedSchool: Boolean? = null,
    onHasAttendedSchoolChanged: (Boolean) -> Unit = {},
    showHigherEducationDropdown: Boolean = false,
    onShowHighEducationDropdownChange: (Boolean) -> Unit = {},
    highestEducationLevel: String = "",
    onHighestEducationLevelChanged: (String) -> Unit = {},
    parentGender: String = "",
    onParentGenderChanged: (String) -> Unit = {},
    showGuardianGenderDropdown: Boolean = false,
    onShowGuardianGenderDropdownChanged: (Boolean) -> Unit = {},
    type: String = "",
    onTypeChanged: (String) -> Unit = {},
    showTypeDropdown: Boolean = false,
    onShowTypeDropdownChanged: (Boolean) -> Unit = {},
    onAddParent: () -> Unit,
    onRemoveParent: (Parent) -> Unit = {},
    parents: MutableList<Parent>,
    showAddChildSheet: Boolean = false,
    onShowAddChildSheetChange: (Boolean) -> Unit = {},
    childFirstName: String = "",
    onChildFirstNameChanged: (String) -> Unit = {},
    childLastName: String,
    onChildLastNameChanged: (String) -> Unit,
    childGender: String = "",
    onChildGenderChanged: (String) -> Unit = {},
    childAge: String,
    childAgeError: String?,
    onChildAgeChanged: (String) -> Unit,
    showChildGenderDropdown: Boolean = false,
    onShowChildGenderDropdownChanged: (Boolean) -> Unit = {},
    livesWith: String = "",
    linkedLearnerId: String,
    childGrade: String,
    childGradeError: String?,
    showChildGradeDropdown: Boolean = false,
    onShowChildGradeDropdownChanged: (Boolean) -> Unit,
    onChildGradeChanged: (String) -> Unit,
    linkedIdList: MutableList<String> = mutableListOf(),
    onLinkedLearnerIdChange: (String) -> Unit,
    showAvailableLearnersDropdown: Boolean,
    onShowAvailableLearnersDropdownChanged: (Boolean) -> Unit,
    onLivesWithChanged: (String) -> Unit = {},
    showLivesWithDropdown: Boolean = false,
    onShowLivesWithDropdownChanged: (Boolean) -> Unit = {},
    onAddChild: () -> Unit,
    onRemoveChild: (Child) -> Unit = {},
    children: MutableList<Child>,
    availableLearners: List<NyansapoStudent> = emptyList(),
    error: String?

    ) {
    val types = remember { listOf("Father", "Mother", "Guardian") }

    val genderOptions = remember { listOf("Female","Intersex", "Male") }
    val livesWithOptions = remember { listOf("Mother", "Father", "Both Mother and Father", "Guardian") }

    val educationOptions = remember {
        listOf(
            "Never attended school",
            "Not Completed Primary School",
            "Completed Primary School",
            "Not Completed Secondary School",
            "Completed Secondary School",
            "Beyond Secondary School"
        )
    }


    val gradesAllowed = remember { listOf("3", "4", "5",)}

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    AnimatedVisibility(
        visible = showParentOrGuardianSheet
    )
    {
        ModalBottomSheet(
            onDismissRequest = { onShowParentOrGuardianSheetChange(false) },
            sheetState = sheetState,
            containerColor = lightPrimary,
            modifier = Modifier
                .fillMaxSize()
                .imePadding()

        ){
            LazyColumn (
                modifier = Modifier
                    .imePadding()
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        text = "Add Parent/Guardian",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }


                item {
                    AppTextField(
                        required = true,
                        label = "Name",
                        value = parentName,
                        error = parentNameError,
                        onValueChanged = onParentNameChanged,
                        placeholder = "Enter name"
                    )

                }

                item {
                    AppTextField(
                        required = true,
                        label = "Age",
                        value = parentAge,
                        error = parentAgeError,
                        onValueChanged = onParentAgeChanged,
                        keyboardType = KeyboardType.Number,
                        placeholder = "Enter age"
                    )
                }


                item {
                    AppDropDownMenu(
                        required = true,
                        expanded = showGuardianGenderDropdown,
                        label = "What is the gender?",
                        placeholder = "Select gender",
                        onClick = { onShowGuardianGenderDropdownChanged(!showGuardianGenderDropdown) },
                        value = parentGender
                    ) {
                        genderOptions.forEach { gender ->
                            AppDropDownItem(
                                item = gender,
                                isSelected = parentGender == gender,
                                onClick = { onParentGenderChanged(gender) }
                            )
                        }
                    }
                }


                item {
                    AppDropDownMenu(
                        required = true,
                        expanded = showTypeDropdown,
                        label = "Type",
                        placeholder = "Select type",
                        onClick = { onShowTypeDropdownChanged(!showTypeDropdown) },
                        value = type
                    ) {
                        types.forEach { t ->
                            AppDropDownItem(
                                item = t,
                                isSelected = type == t,
                                onClick = { onTypeChanged(t) }
                            )
                        }
                    }
                }


                item {
                    YesNoOption(
                        text = "Has ever attended school?",
                        isYes = hasAttendedSchool ,
                        onChange = {onHasAttendedSchoolChanged(it)}
                    )
                }


                item {
                    AnimatedVisibility(
                        visible = hasAttendedSchool == true
                    ) {
                        AppDropDownMenu(
                            required = true,
                            expanded = showHigherEducationDropdown,
                            label = "Highest education level of the guardian",
                            placeholder = "Select education level",
                            onClick = { onShowHighEducationDropdownChange(!showHigherEducationDropdown) },
                            value = highestEducationLevel
                        ) {
                            educationOptions.forEach { level ->
                                AppDropDownItem(
                                    item = level,
                                    isSelected = highestEducationLevel == level,
                                    onClick = { onHighestEducationLevelChanged(level) }
                                )
                            }
                        }
                    }
                }


                item {
                    AppButton(
                        enabled = parentName.isNotBlank() && parentAge.isNotBlank() && parentGender.isNotBlank() && type.isNotBlank() && (hasAttendedSchool == false || highestEducationLevel.isNotBlank()) && parentNameError == null && parentAgeError == null,
                        onClick = {
                            onAddParent.invoke()
                        }
                    ) {
                        Text(
                            text = "Add Parent/Guardian",
                        )
                    }
                }


            }
        }
    }

    AnimatedVisibility(
        visible = showAddChildSheet
    )
    {
        ModalBottomSheet(
            onDismissRequest = { onShowAddChildSheetChange(false) },
            sheetState = sheetState,
            containerColor = lightPrimary,
            modifier = Modifier
                .fillMaxSize()
                .imePadding()

        ){
            LazyColumn (
                modifier = Modifier
                    .imePadding()
                    .padding(16.dp)
            ) {
                item{
                    Text(
                        text = "Add Child",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }


                item{
                    AppTextField(
                        required = true,
                        label = "First Name of the child",
                        value = childFirstName,
                        onValueChanged = onChildFirstNameChanged,
                        placeholder = "Enter child's first name"
                    )
                }
                item{
                    AppTextField(
                        required = true,
                        label = "Last Name of the child",
                        value = childLastName,
                        onValueChanged = onChildLastNameChanged,
                        placeholder = "Enter child's last name"
                    )
                }

                item {
                    AppDropDownMenu(
                        required = true,
                        expanded = showChildGenderDropdown,
                        label = "Gender of the child",
                        placeholder = "Select gender",
                        onClick = { onShowChildGenderDropdownChanged(!showChildGenderDropdown) },
                        value = childGender
                    ) {
                        genderOptions.forEach { gender ->
                            AppDropDownItem(
                                item = gender,
                                isSelected = childGender == gender,
                                onClick = { onChildGenderChanged(gender) }
                            )
                        }
                    }
                }

                item {
                    AppTextField(
                        required = true,
                        label = "Age of the child",
                        value = childAge,
                        error = childAgeError,
                        onValueChanged = onChildAgeChanged,
                        keyboardType = KeyboardType.Number,
                        placeholder = "The child age should be between 6 and 17"
                    )
                }
                item {
                    AppDropDownMenu(
                        required = true,
                        expanded = showChildGradeDropdown,
                        label = "Grade of the child",
                        placeholder = "Select grade",
                        onClick = { onShowChildGradeDropdownChanged(!showChildGradeDropdown) },
                        value = childGrade
                    ) {
                        gradesAllowed.forEach {
                            AppDropDownItem(
                                item = "Grade $it",
                                isSelected = childGrade == it,
                                onClick = { onChildGradeChanged(it) }
                            )
                        }
                    }

                }


                item {
                    AppDropDownMenu(
                        required = true,
                        expanded = showLivesWithDropdown,
                        label = "The child lives with",
                        placeholder = "Select who the child lives with",
                        onClick = { onShowLivesWithDropdownChanged(!showLivesWithDropdown) },
                        value = livesWith
                    ) {
                        livesWithOptions.forEach { option ->
                            AppDropDownItem(
                                item = option,
                                isSelected = livesWith == option,
                                onClick = { onLivesWithChanged(option) }
                            )
                        }
                    }
                }


                item {
                    AppDropDownMenu(
                        required = true,
                        expanded = showAvailableLearnersDropdown,
                        label = "Link the child to",
                        placeholder = "Select one ",
                        onClick = { onShowAvailableLearnersDropdownChanged(!showAvailableLearnersDropdown) },
                        value = if (linkedLearnerId.isNotBlank()) "Linked" else "Not Linked"
                    ) {
                        if (availableLearners.isEmpty()){
                            Text(
                                text = "No available learners",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            return@AppDropDownMenu
                        }

                        availableLearners.forEach { learner ->
                            AppDropDownItem(
                                item = learner.name.ifEmpty { learner.first_name + " " + learner.last_name },
                                isSelected = linkedLearnerId == learner.id,
                                onClick = { onLinkedLearnerIdChange(learner.id) }
                            )
                        }
                    }
                }


                item {
                    AppButton(
                        enabled =childLastName.isNotBlank() && childFirstName.isNotBlank() && childGender.isNotBlank() && livesWith.isNotBlank() && childAge.isNotBlank() && linkedLearnerId.isNotBlank() && childAgeError == null && childGradeError == null && childGrade.isNotBlank(),
                        onClick = {
                            onAddChild.invoke()
                        }
                    ) {
                        Text(
                            text = "Add Child",
                        )
                    }
                }


            }
        }
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .imePadding()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            Text(
                text = "Parent/Guardian",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Please provide information about your parent or guardian.",
                style = MaterialTheme.typography.bodyMedium,
            )


            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = lightPrimary
                    ) ,
                    onClick = {onShowParentOrGuardianSheetChange(true)}
                ) {
                    Text(
                        text = "Add Parent/Guardian",
                        style = MaterialTheme.typography.bodyMedium.copy(
                        )
                    )
                }

                parents.forEach {
                    OptionsItemContent(
                        isSelected = true,
                        onClick = {
                            onRemoveParent(it)
                        }
                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${it.type}: ${it.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Icon(
                                painter = painterResource(R.drawable.close),
                                contentDescription = "remove parent",
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            onRemoveParent(it)
                                        }
                                    )
                            )
                        }
                    }
                }
            }


        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            Text(
                text = "Children",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Please provide information about your school-age children.",
                style = MaterialTheme.typography.bodyMedium,
            )

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = lightPrimary
                    ) ,
                    onClick = {onShowAddChildSheetChange(true)}
                )
                {
                    Text(
                        text = "Add Child",
                        style = MaterialTheme.typography.bodyMedium.copy(
                        )
                    )
                }

                children.forEach {
                    OptionsItemContent(
                        isSelected = true,
                        onClick = {}
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = it.firstName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )

                            Icon(
                                painter = painterResource(R.drawable.close),
                                contentDescription = "remove child",
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            onRemoveChild(it)
                                        }
                                    )
                            )
                        }
                    }
                }
            }


        }

        AnimatedVisibility(
            visible = error != null
        ){
            Text(
                text = error ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}