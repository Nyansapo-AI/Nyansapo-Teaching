package com.nyansapoai.teaching.presentation.home

import com.nyansapoai.teaching.presentation.home.components.BottomNavigationItem

data class HomeState(
    val currentNavigationItem: BottomNavigationItem = BottomNavigationItem.appBottomNavItems[0]
)