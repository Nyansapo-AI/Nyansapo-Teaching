package com.nyansapoai.teaching.presentation.common.animations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.nyansapoai.teaching.R

@Composable
fun RiveAnimation(
    modifier: Modifier = Modifier,
    resId: Int
) {
    var riveView by remember { mutableStateOf<RiveAnimationView?>(null) }

    AndroidView(
        factory = { context ->
            RiveAnimationView(context = context).apply {
                setRiveResource(resId = resId)
                riveView = this
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            riveView?.pause()
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RiveAnimationPreview(){
    RiveAnimation(
        resId = R.raw.fingerpointer
    )
}