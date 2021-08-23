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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.toast
import com.github.crayonxiaoxin.wanandroid.ui.common.Dialog
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadingDialog
import com.github.crayonxiaoxin.wanandroid.ui.common.RequestPermissions
import com.github.crayonxiaoxin.wanandroid.util.User
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
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
        val focusManager = LocalFocusManager.current
        val focusRequester = remember {
            FocusRequester()
        }
        val softwareKeyboardController = LocalSoftwareKeyboardController.current
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
                    focusManager.clearFocus()
//                    softwareKeyboardController?.hide()
                }),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            softwareKeyboardController?.hide()
                        }
                    }
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
                Dialog(
                    modifier = Modifier.width(300.dp),
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    titleText = "title",
                    messageText = "Here is the message",
                    okClick = {
                        openDialog.value = false
                    },
                    cancelClick = {
                        openDialog.value = false
                    }
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

            val openLoading = remember {
                mutableStateOf(false)
            }
            Button(onClick = {
                openLoading.value = true
            }
            ) {
                Text(text = "Show Loading Dialog")
            }
            if (openLoading.value) {
                LoadingDialog(text = "加載中", onDismissRequest = { openLoading.value = false })
            }

            val scope = rememberCoroutineScope()
            Button(onClick = {
                scope.launch {
                    User.isLogin(false)
                }
                controller.navigate("login") {
                    // 跳转到 login 之前，先出栈到 main，包括 main
                    popUpTo("main") { inclusive = true }
                }
            }
            ) {
                Text(text = "Logout")
            }

            DropdownDemo()


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

@Composable
fun DropdownDemo() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(Color.Gray)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
            }
        }
    }
}


