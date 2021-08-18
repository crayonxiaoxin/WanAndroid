package com.github.crayonxiaoxin.wanandroid.ui.tixi

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.toast
import com.github.crayonxiaoxin.wanandroid.ui.common.RequestPermission
import com.github.crayonxiaoxin.wanandroid.ui.common.RequestPermissions
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.permissions.*

@OptIn(ExperimentalUnitApi::class)
@Composable
fun TixiScreen(controller: NavHostController) {
    Scaffold(topBar = {
        TixiTopBar(
            background = Color.White,
            selectedColor = Color.Black
        )
    }) {
        var value by remember {
            mutableStateOf("1231")
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = value)
            BasicTextField(
                value = value,
                onValueChange = { value = it },
                maxLines = 2,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Blue, fontWeight = FontWeight.Bold, fontSize = 16.sp
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = {
                    toast("enter: $value")
                }),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                    .fillMaxWidth()
                    .padding(10.dp),
            )
            RequestPermissionsDemo()
            val openDialog = remember {
                mutableStateOf(false)
            }
            Button(onClick = {
                openDialog.value = true
            }
            ) {
                Text(text = "Show Dialog")
            }
            if (openDialog.value) {
                AlertDialog(
                    modifier = Modifier.width(300.dp),
                    onDismissRequest = {
                        // user click outside the dialog or backPress
                        openDialog.value = false
                    },
                    title = {
                        Text(
                            text = "Prompt",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    buttons = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                "取消",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .clickable { openDialog.value = false }
                                    .padding(10.dp)
                                    .weight(1f)
                            )
                            Text(
                                "确定",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .clickable { openDialog.value = false }
                                    .padding(10.dp)
                                    .weight(1f)
                            )
                        }
                    },
                    text = {
                        Text(
                            text = "Here is the message",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                )
            }
            val openPopup = remember {
                mutableStateOf(false)
            }
            Button(onClick = {
                openPopup.value = true
            }
            ) {
                Text(text = "Show Popup")
            }
            if (openPopup.value) {
                Popup(
                    alignment = Alignment.BottomCenter,
                    onDismissRequest = { openPopup.value = false }
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Color.Cyan)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Here is the message",
                            modifier = Modifier
                                .clickable { openPopup.value = false }
                                .padding(10.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Here is the message",
                            modifier = Modifier
                                .clickable { openPopup.value = false }
                                .padding(10.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun TixiTopBar(
    background: Color = Color.White,
    selectedColor: Color = Color.White,
    unSelectedColor: Color = Color.Gray
) {
    Column(
        modifier = Modifier
            .shadow(2.dp)
            .background(color = background)
    ) {
        val selectedIndex = remember {
            mutableStateOf(0)
        }
        // 并不能显示原始background
        TabRow(
            selectedTabIndex = selectedIndex.value,
            modifier = Modifier
                .statusBarsPadding()
                .height(60.dp),
            backgroundColor = background,
            contentColor = background,
            indicator = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent)
                )
            }
        ) {
            Tab(
                selected = selectedIndex.value == 0,
                modifier = Modifier.fillMaxHeight(),
                unselectedContentColor = unSelectedColor,
                selectedContentColor = selectedColor,
                onClick = { selectedIndex.value = 0 }) {
                Text(text = "体系")
            }
            Tab(
                selected = selectedIndex.value == 1,
                modifier = Modifier.fillMaxHeight(),
                unselectedContentColor = unSelectedColor,
                selectedContentColor = selectedColor,
                onClick = { selectedIndex.value = 1 }) {
                Text(text = "导航")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionsDemo() {
//    RequestPermission(
//        permission = Manifest.permission.CAMERA,
//        revoked = {
//            toast("Camera Permission not Granted")
//        },
//        granted = {
//            toast("Camera Permission Granted")
//        }
//    )

    RequestPermissions(
        permissions = arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ),
        revoked = {
            it.forEach {
                if (it == Manifest.permission.CAMERA) {
                    toast("Camera permission not Granted")
                }
                if (it == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    toast("Write permission not Granted")
                }
            }
        },
        granted = {
            toast("Camera and Write permission Granted")
        }
    )
}