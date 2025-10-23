package com.nyansapoai.teaching.presentation.survey.household

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyansapoai.teaching.R
import com.nyansapoai.teaching.domain.models.survey.HouseHoldInfo
import com.nyansapoai.teaching.navController
import com.nyansapoai.teaching.navigation.AddHouseHoldPage
import com.nyansapoai.teaching.presentation.common.components.AppCircularLoading
import com.nyansapoai.teaching.ui.theme.lightPrimary
import org.koin.androidx.compose.koinViewModel

@Composable
fun HouseholdRoot() {

    val viewModel = koinViewModel<HouseholdViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HouseholdScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdScreen(
    state: HouseholdState,
    onAction: (HouseholdAction) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Households",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(
                            onClick = {
                                navController.navigate(AddHouseHoldPage)
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary,
                                containerColor = lightPrimary
                            )
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add),
                                contentDescription = "Add Household"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { innerPadding ->

        AnimatedVisibility(
            visible = state.isLoading
        ) {
            AppCircularLoading()
        }


        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ){
            if (state.households.isEmpty()){
                item {
                    Text(
                        text = "No households available. Please add a household.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(vertical = 24.dp)
                    )

                    return@item
                }
            }

            items(items = state.households){ household ->
                HouseholdListItem(
                    household = household
                )
            }

        }

    }
}


@Composable
fun HouseholdListItem(
    modifier: Modifier = Modifier,
    household: HouseHoldInfo
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)

    ) {
        Column {
            Text(
                text = household.householdHeadName?.let { "$it's Home" } ?: "Unnamed Home",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Family Members: ${household.householdMembersCount}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.6f
                )
            )

        }


        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = "View Household"
            )
        }
    }
}