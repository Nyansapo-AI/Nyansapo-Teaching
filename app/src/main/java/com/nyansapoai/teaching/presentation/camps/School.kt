package com.nyansapoai.teaching.presentation.camps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.presentation.common.components.AppComingSoon
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

    AnimatedVisibility(
        visible = state.showSchoolSelector
    ) {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = rememberModalBottomSheetState()
        ) {
            LazyColumn {
                stickyHeader {
                    Text(
                        text = "Select School"
                    )
                }

                items(items = state.user?.organizations[0]?.projects[0]?.schools ?: emptyList(), key = {it.id}){ school ->
                    Text(
                        text = school.name
                    )
                }
            }
        }

    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxSize()
    ) {
        stickyHeader{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
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
                    onClick = {}
                ) {
                    Text(
                        text = "School",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
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
        }*/


        item{
            AppComingSoon(
                modifier = Modifier
                    .size(400.dp)
            )
        }


    }

}