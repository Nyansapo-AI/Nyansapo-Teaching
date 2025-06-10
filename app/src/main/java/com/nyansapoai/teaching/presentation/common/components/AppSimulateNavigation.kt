package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> AppSimulateNavigation(
    modifier: Modifier = Modifier,
    targetState: T,
    content: @Composable () -> Unit = {},
) {

    AnimatedContent(
        modifier = modifier,
        targetState = targetState,
        transitionSpec = {
            fadeIn(animationSpec = tween(300, 300))+ slideInHorizontally(animationSpec = tween(300, 300)) togetherWith fadeOut(animationSpec = tween(150))
        },
        label = "transitionSpec"
    ) { state ->
        when(state){
            else -> {
                content()
            }
        }
    }

}