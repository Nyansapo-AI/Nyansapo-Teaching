package com.nyansapoai.teaching.presentation.home

import com.nyansapoai.teaching.presentation.home.components.BottomNavigationItem

sealed interface HomeAction {
    data class OnChangeScreen(val bottomNavigationItem: BottomNavigationItem): HomeAction

}