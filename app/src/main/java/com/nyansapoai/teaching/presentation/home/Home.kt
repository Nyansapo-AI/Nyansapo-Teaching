package com.nyansapoai.teaching.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nyansapoai.teaching.presentation.common.components.getDeviceWidth
import com.nyansapoai.teaching.presentation.home.components.BottomNavigationItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot() {

    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {


    val deviceWidth = getDeviceWidth()

    Scaffold(
        bottomBar = {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                BottomNavigationItem.appBottomNavItems.forEach { item ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                        interactionSource = remember { MutableInteractionSource() },
                        modifier =
                            Modifier
                                .testTag(item.title),
                        selected = state.currentNavigationItem == item,
                        onClick = {
                            onAction.invoke(HomeAction.OnChangeScreen(bottomNavigationItem = item))
                        },
                        label = {
                            Text(
                                text = item.title,
                                color =
                                    if (item == state.currentNavigationItem) {
                                        MaterialTheme.colorScheme.secondary
                                    } else {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    },
                            )
                        },
                        alwaysShowLabel = deviceWidth > 420,
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.title,
                                tint =
                                    if (item == state.currentNavigationItem) {
                                        MaterialTheme.colorScheme.secondary
                                    } else {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                    },
                            )
                        },
                    )

                }

            }
        },
        modifier = Modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            state.currentNavigationItem.screen()
        }
    }

}