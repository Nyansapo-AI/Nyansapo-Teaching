package com.nyansapoai.teaching.presentation.assessments.literacy.result.components

import android.webkit.WebView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.nyansapoai.teaching.presentation.assessments.literacy.components.generateComparisonHtml
import java.nio.file.WatchEvent

@Preview(showBackground = true)
@Composable
fun ParagraphResultItem(
    modifier: Modifier = Modifier,
    expected: String = "Expected",
    studentAnswer: String = "Student Answer",
    onClick: () -> Unit = {  },
) {
//    val context = LocalContext.current

    val result = generateComparisonHtml(
        expected = expected,
        actual = studentAnswer
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(8.dp)
//            .border(
//                width = 4.dp,
//                color = color,
//                shape = RoundedCornerShape(8.dp)
//            )
            .padding(16.dp)
            .zIndex(0.8f)
            .clickable(
                onClick = onClick,
            )
    ) {

        /*
        Text(
            AnnotatedString.fromHtml(
                htmlString = result
            ),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )*/

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = false
                    loadDataWithBaseURL(
                        null,
                        result,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

    }

}