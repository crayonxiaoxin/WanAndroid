package com.github.crayonxiaoxin.wanandroid.ui.daohang

import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.toast
import com.github.crayonxiaoxin.wanandroid.ui.common.DetailTopBar
import com.github.crayonxiaoxin.wanandroid.util.DateUtil
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DaoHangScreen(controller: NavHostController) {
    val (gesturesEnabled, toggleGesturesEnabled) = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            DetailTopBar(
                modifier = Modifier
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(bottomEnd = 30.dp))
                    .background(color = Color(255, 255, 255)),
                titleLabel = "导航"
            )
        },
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = gesturesEnabled,
                        onValueChange = toggleGesturesEnabled
                    )
            ) {
                Checkbox(gesturesEnabled, onCheckedChange = null)
                Text(text = if (gesturesEnabled) "Gestures Enabled" else "Gestures Disabled")
            }
            val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
            BottomDrawer(
                gesturesEnabled = gesturesEnabled,
                drawerState = drawerState,
                drawerBackgroundColor = Color.Transparent, // 改变默认颜色
                drawerContent = {
                    Column(
                        modifier = Modifier
                            .shadow(
                                4.dp,
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                            ) // 圆角
                            .fillMaxWidth()
                            .background(color = Color.White)
                    ) {
                        Button(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 16.dp),
                            onClick = { scope.launch { drawerState.close() } },
                            content = { Text("Close Drawer") }
                        )
                        LazyColumn {
                            items(5) {
                                ListItem(
                                    text = { Text("Item $it") },
                                    icon = {
                                        Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = "Localized description"
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val openText =
                            if (gesturesEnabled) "▲▲▲ Pull up ▲▲▲" else "Click the button!"
                        Text(text = if (drawerState.isClosed) openText else "▼▼▼ Drag down ▼▼▼")
                        Spacer(Modifier.height(20.dp))
                        Button(onClick = { scope.launch { drawerState.open() } }) {
                            Text("Click to open")
                        }
                        DatePickerDialogDemo()
                    }
                }
            )
        }
    }
}

@Composable
fun DatePickerDialogDemo() {

    val openDatePicker = remember {
        mutableStateOf(false)
    }
    Button(onClick = { openDatePicker.value = true }) {
        Text("Click to open DatePicker")
    }
    if (openDatePicker.value) {
        Dialog(onDismissRequest = {
            openDatePicker.value = false
        }) {
            AndroidView(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = Color.White),
                factory = {
                    DatePicker(it).apply {
                        minDate = DateUtil.getDateByFormat(
                            date = DateUtil.getDateFormat(
                                date = DateUtil.getMonthAgo(-1)
                            )
                        ).time
                        val currentDate = DateUtil.getCurrentDate()
                        val c = Calendar.getInstance()
                        c.time = currentDate
                        maxDate = currentDate.time
                        init(
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH)
                        ) { _, year, monthOfYear, dayOfMonth ->
                            toast("$year - ${monthOfYear + 1} - $dayOfMonth")
                            openDatePicker.value = false
                        }
                    }
                }
            )
        }
    }

}
