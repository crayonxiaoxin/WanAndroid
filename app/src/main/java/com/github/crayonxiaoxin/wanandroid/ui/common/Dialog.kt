package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun Dialog(
    onDismissRequest: () -> Unit, // user click outside the dialog or backPress
    modifier: Modifier = Modifier,
    titleText: String = "",
    messageText: String = "",
    cancelText: String = "Cancel",
    okText: String = "OK",
    cancelClick: () -> Unit = {},
    okClick: () -> Unit = {},
    cancelTextColor: Color = Color.Gray,
    okTextColor: Color = Color.Black,
    title: (@Composable () -> Unit)? = {
        Text(
            text = titleText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    },
    message: @Composable (() -> Unit)? = {
        Text(
            text = messageText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    },
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    properties: DialogProperties = DialogProperties()
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = title,
        shape = shape,
        buttons = {
            Column {
                Divider(color = Color.LightGray)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .clickable(onClick = cancelClick)
                            .fillMaxHeight()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            cancelText,
                            color = cancelTextColor,
                            textAlign = TextAlign.Center
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(color = Color.LightGray)
                    )
                    Box(
                        modifier = Modifier
                            .clickable(onClick = okClick)
                            .fillMaxHeight()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            okText,
                            color = okTextColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        text = message,
        contentColor = contentColor,
        properties = properties
    )
}
