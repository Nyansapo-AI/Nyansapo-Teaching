package com.nyansapoai.teaching.presentation.schools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.presentation.common.components.AppAlertDialog
import com.nyansapoai.teaching.presentation.common.components.AppCardItem
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.presentation.common.components.AppComingSoon
import com.nyansapoai.teaching.presentation.schools.components.LearningLevelItem
import com.nyansapoai.teaching.ui.theme.lightPrimary
import org.koin.androidx.compose.koinViewModel

@Composable
fun CampRoot() {

    val viewModel = koinViewModel<SchoolViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    SchoolScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolScreen(
    state: SchoolState,
    onAction: (SchoolAction) -> Unit,
) {

    LaunchedEffect(state.localSchoolInfo) {
        onAction.invoke(
            SchoolAction.OnFetchSchoolDetails(
                organizationId = state.localSchoolInfo?.organizationUid ?: "",
                projectId = state.localSchoolInfo?.projectUId ?: "",
                schoolId = state.localSchoolInfo?.schoolUId ?: ""
            )
        )
    }

    AnimatedVisibility(
        visible = state.showLogOutDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AppAlertDialog(
            onDismissRequest = { onAction.invoke(SchoolAction.OnShowLogOutDialog(false)) },
            dialogText = "You are about to sign out. Click confirm to continue.",
            dialogTitle = "Sign Out",
//            icon = Icons.Default.Info,
            onConfirmation = {
                onAction.invoke(SchoolAction.OnShowLogOutDialog(false))
                onAction.invoke(SchoolAction.SignOut)
            },
        )
    }


    AnimatedVisibility(
        visible = state.showSchoolSelector
    ) {
        ModalBottomSheet(
            containerColor = lightPrimary,
            onDismissRequest = {
                onAction.invoke(SchoolAction.OnShowSchoolSelector(show = false))
            },
            sheetState = rememberModalBottomSheetState(),
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 12.dp)
            ) {
                stickyHeader {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.school),
                            contentDescription = state.schoolDetails?.name ?: "School",
                            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
                        )

                        Text(
                            text = stringResource(R.string.select_school),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                item{
                    Spacer(Modifier.padding(12.dp))
                }

                items(items = state.user?.organizations[0]?.projects[0]?.schools ?: emptyList(), key = {it.id}){ school ->
                    TextButton(
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        onClick = {

                        }
                    ) {
                        Text(
                            text = school.name
                        )
                    }
                }

                item {
                    TextButton(
                        onClick = {
                            onAction.invoke(SchoolAction.OnShowLogOutDialog(true))
                        }
                    ) {
                        Text(
                            text = "Sign out",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                }

            }
        }
    }

    AnimatedVisibility(
        visible = state.isLoading
    ) {
        AppCircularLoading()
        return@AnimatedVisibility
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
    )
    {
        stickyHeader{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 12.dp)

            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = state.greeting,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = state.user?.name ?: "Unknown Name",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }


                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = {
                        onAction.invoke(SchoolAction.OnShowSchoolSelector(show = !state.showSchoolSelector))
                    },
                ) {
                    Text(
                        text = "School",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    AppCardItem(
                        title = if (state.schoolDetails?.total_students == 1) "Student" else "Students",
                        count = state.schoolDetails?.total_students,
                        imageResId = R.drawable.animated_student,
                        modifier = Modifier
                            .padding(start = 12.dp)
                    )
                }

                item {
                    AppCardItem(
                        title = if (state.schoolDetails?.total_teachers == 1) "Teacher" else "Teachers",
                        count = state.schoolDetails?.total_teachers,
                        imageResId = R.drawable.animated_teacher,
                        modifier = Modifier
                            .padding(end = 12.dp)
                    )
                }
            }
        }

        /*
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = state.camp.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )



                FlowRow(
                    itemVerticalAlignment = Alignment.CenterVertically,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    state.camp.learning_level_description.forEach {
                        LearningLevelItem(
                            levelDescription = it
                        )
                    }

                }

            }
        } */


        /*
        item{
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "School Assessment Results",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )

                Spacer(Modifier.padding(12.dp))


                LearningLevelItem(
                    levelDescription = LearningLevelDescription(
                        type = "Literacy",
                        totalStudents = 15,
                        data = listOf(
                            Level("Letters", 1, Color.Blue),
                            Level("Words", 5, Color.Cyan),
//                            Level("Sentence", 3, Color.Green),
                            Level("Paragraph", 4, Color(0xFFC8DF47)),
                            Level("Story", 5, Color.Magenta),
                        )
                    )
                )

                Spacer(Modifier.padding(12.dp))

                LearningLevelItem(
                    levelDescription = LearningLevelDescription(
                        type = "Numeracy",
                        totalStudents = 21,
                        data = listOf(
                            Level("Number Recognition", 1, Color.Blue),
                            Level("Counting", 2, Color.Cyan),
                            Level("Addition", 3, Color.Green),
                            Level("Subtraction", 4, Color(0xFFC8DF47)),
                            Level("Multiplication", 5, Color.Magenta),
                            Level("Division", 6, Color.Yellow),
                        )
                    )
                )
            }
        }*/

         */



    }

}