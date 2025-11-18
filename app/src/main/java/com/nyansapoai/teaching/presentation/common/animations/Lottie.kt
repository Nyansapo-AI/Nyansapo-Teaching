package com.nyansapoai.teaching.presentation.common.animations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nyansapoai.teaching.R

@Composable
fun AppLottieAnimations(
    modifier: Modifier = Modifier,
    resId: Int = R.raw.story_loading,
    size: Dp = 400.dp
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(composition)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
    ){
        LottieAnimation(
            composition = composition,
//        progress = { progress },
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.fillMaxSize()
        )

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LottieLoadingPreview() {
    AppLottieAnimations()
}