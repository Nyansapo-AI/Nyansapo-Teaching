package com.nyansapoai.teaching.presentation.common.animations

import androidx.annotation.RawRes
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
import app.rive.runtime.kotlin.core.Alignment
import app.rive.runtime.kotlin.core.Fit
import app.rive.runtime.kotlin.core.Rive
import com.nyansapoai.teaching.R

@Composable
fun RiveAnimation(
    modifier: Modifier = Modifier,
    @RawRes animation: Int,
    artBoardName: String? = null,
) {


    var riveView by remember { mutableStateOf<RiveAnimationView?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->

            /*
            val builder = RiveAnimationView.Builder(context = context)
                .setResource(
                    R.raw.firstrive
                )
                .setArtboardName(value = "8513340-removebg-preview")
                .setAutoplay(true)




            builder.build().apply {
                riveView = this
            }

             */

            /*
            RiveAnimationView(context = context).apply {
                setRiveResource(
                    resId = R.raw.firstrive,
                    artboardName = "8513340-removebg-preview"
                )
                autoplay = true
//                setBackgroundColor(Color.RED)
                riveView = this
            }

             */

            RiveAnimationView(context = context).also { view ->
                view.setRiveResource(
                    resId = animation,
                    artboardName = artBoardName,
                    alignment = Alignment.CENTER_RIGHT,
                    fit = Fit.CONTAIN
                )
            }


        }
    )

    DisposableEffect(Unit) {
        onDispose {
            riveView?.pause()
        }
    }

}