package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.github.crayonxiaoxin.wanandroid.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * loading
 */
@Composable
fun LoadingDialog(
    text: String? = "Loading",
    size: Dp = 32.dp,
    delay: Long = 30L,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties()
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color(0, 0, 0, 0x49))
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val state = remember {
                mutableStateOf(0f)
            }
            val scope = rememberCoroutineScope()
            SideEffect {
                scope.launch {
                    delay(delay)
                    if (state.value == 330f) {
                        state.value = 0f
                    } else {
                        state.value += 30f
                    }
                }
            }
            Image(
                modifier = Modifier
                    .size(size)
                    .rotate(state.value),
                painter = painterResource(id = R.drawable.dialog_loading_img),
                contentDescription = ""
            )
            text?.let {
                Text(
                    it,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}