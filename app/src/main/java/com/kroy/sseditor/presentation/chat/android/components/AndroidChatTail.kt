package com.kroy.sseditor.presentation.chat.android.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun AndroidChatTail(
    modifier: Modifier = Modifier.size(300.dp, 200.dp),
    color: Color = Color.Red,
    isSender: Boolean = true
) {

    Canvas(
        modifier = modifier
            .graphicsLayer {
                if (isSender) scaleX = -1f
            }
    ) {

        val h = size.height
        val w = size.width

        val path = Path().apply {
            moveTo(2f, h)
            quadraticTo(-2f, (9.5f) * h / 10, 2f, 9 * h / 10)
            quadraticTo(6 * w / 10, 7 * h / 10, 10 * w / 10, 0 * h / 10)
            lineTo(10 * w / 10, h)
        }

        drawPath(path = path, color = color)

    }


}

