package com.nyansapoai.teaching.presentation.common.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyansapoai.teaching.R

@Composable
fun StepsRow(
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    numberOfSteps: Int,
    currentStep: Int,
    stepDescriptionList: List<StepContent>,
    unSelectedColor: Color = Color.LightGray,
    selectedColor: Color? = null
){
    val descriptionList = MutableList(numberOfSteps) { "" }

    stepDescriptionList.forEachIndexed { index, element ->
        if (index < numberOfSteps)
            descriptionList[index] = element.title
    }


    LazyRow(
        state = state,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){

        items(numberOfSteps){ step ->
            Step(
                modifier = Modifier,
                step = step,
                isCompete = step < currentStep - 1,
                isCurrent = step == currentStep - 1,
                isComplete = step == numberOfSteps - 1,
                stepDescription = descriptionList[step],
                unSelectedColor = unSelectedColor,
                selectedColor = selectedColor,
                onClick = onClick
            )
        }

    }
}


@Composable
private fun Step(
    modifier: Modifier = Modifier,
    step: Int,
    isCompete: Boolean,
    isCurrent: Boolean,
    isComplete: Boolean,
    stepDescription: String,
    unSelectedColor: Color,
    onClick: (Int) -> Unit,
    selectedColor: Color?,
) {



    val transition = updateTransition(isCompete, label = "")

    val innerCircleColor by transition.animateColor(label = "innerCircleColor") {
        if (it || isCurrent) selectedColor ?: MaterialTheme.colorScheme.secondary else unSelectedColor
    }
    val txtColor by transition.animateColor(label = "txtColor")
    { if (it || isCurrent) selectedColor ?: MaterialTheme.colorScheme.secondary else unSelectedColor }

    val color by transition.animateColor(label = "color")
    { if (it || isCurrent) selectedColor ?: MaterialTheme.colorScheme.secondary else Color.Gray }

    val borderStroke = BorderStroke(2.dp, color)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(end = 12.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(3))
            .clickable { onClick.invoke(step + 1) },
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            shape = CircleShape,
            border = borderStroke,
            color = innerCircleColor,
            modifier = Modifier.size(30.dp)
        ) {

            Box(contentAlignment = Alignment.Center) {
                if (isCompete)
                    Icon(
                        painter = painterResource(R.drawable.check), "done",
                        modifier = modifier.padding(4.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                else
                    Text(
                        text = (step + 1 ).toString(),
                        color = if (isCurrent || isComplete) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
            }
        }


        Text(
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = stepDescription,
            color = txtColor,
        )

        if(!isComplete){
            Text(
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = "---",
                color = txtColor,
            )
        }
    }
}