package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun StrokeTag(
    text: String,
    color: Color,
    textStyle: TextStyle = MaterialTheme.typography.caption
) {
    Text(
        text = text,
        color = color,
        style = textStyle,
        modifier = Modifier
            .padding(end = 10.dp)
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 2.dp)
    )
}