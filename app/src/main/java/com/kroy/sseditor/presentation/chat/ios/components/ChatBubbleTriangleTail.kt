package com.kroy.sseditor.presentation.chat.ios.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ReceiverChatBubbleTriangleTail(
    modifier: Modifier = Modifier,
    isSender: Boolean = false,
    color: Color = Color.Black
) {
    Canvas(
        modifier = modifier.graphicsLayer {
            if (isSender) scaleX = -1f
        }
    ) {

        val h = size.height
        val w = size.width

        val path = Path().apply {
            moveTo(0f, h)
            quadraticTo((4 * w) / 8, h / 2, (5.5f * w) / 8, 0f)
//            lineTo(w, 0f)
//            lineTo(w, (5 * h) / 9)
            quadraticTo(6*w/9, (6 * h) / 10, (8f*w)/10, (5 * h) / 9)
            quadraticTo((5 * w) / 8, (7 * h) / 8, 0f, h)

        }

        drawPath(path = path, brush = Brush.linearGradient(listOf(
            Color(0xFF201F24),
            Color(0xFF252024),
            Color(0xFF1B1A1F)
        )), style = Fill)
    }
}



@Composable
fun SenderChatBubbleTriangleTail(
    modifier: Modifier = Modifier,
    isSender: Boolean ,
    color: Color = Color.Black
) {
    Canvas(
        modifier = modifier.graphicsLayer {
            if (isSender) scaleX = -1f
        }
    ) {

        val h = size.height
        val w = size.width

        val path = Path().apply {
            moveTo(0f, h)
            quadraticTo((4 * w) / 8, h / 2, (5 * w) / 8, 0f)
            lineTo(w, 0f)
            lineTo(w, (5 * h) / 9)
            quadraticTo((5 * w) / 8, (7 * h) / 8, 0f, h)

        }

        drawPath(path = path, brush = Brush.linearGradient(listOf(Color(0xFFCD25EE), Color(0xFF9D1EEA) )), style = Fill,)
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewTail(modifier: Modifier = Modifier) {
    SenderChatBubbleTriangleTail(
        modifier = modifier.fillMaxSize(),
        isSender = false,
        color = Color.Black
    )
}
