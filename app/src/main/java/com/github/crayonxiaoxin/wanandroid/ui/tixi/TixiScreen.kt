package com.github.crayonxiaoxin.wanandroid.ui.tixi

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.setWindowAlpha
import com.github.crayonxiaoxin.wanandroid.toast
import com.github.crayonxiaoxin.wanandroid.ui.common.Dialog
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadingDialog
import com.github.crayonxiaoxin.wanandroid.ui.common.PopupBottomPositionProvider
import com.github.crayonxiaoxin.wanandroid.ui.common.RequestPermissions
import com.github.crayonxiaoxin.wanandroid.util.User
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalUnitApi::class, ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun TixiScreen(controller: NavHostController) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }
    Scaffold(
        modifier = Modifier.clickable(
            indication = null, // ??????????????????????????????
            interactionSource = remember { MutableInteractionSource() }
        ) { focusManager.clearFocus() },
        topBar = {
            TixiTopBar(
                background = Color.White,
                selectedColor = Color.Black
            )
        }
    ) {
        var value by remember {
            mutableStateOf("1231")
        }

        val softwareKeyboardController = LocalSoftwareKeyboardController.current
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = value)
            BasicTextField(
                enabled = true,
                value = value,
                onValueChange = { value = it },
                maxLines = 2,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Blue, fontWeight = FontWeight.Bold, fontSize = 16.sp
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    toast("enter: $value")
//                    focusManager.clearFocus()
                    focusManager.moveFocus(FocusDirection.Down)
                }),

                decorationBox = {
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(10.dp)
                    ) {
                        it()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!it.isFocused) {
//                            softwareKeyboardController?.hide()
                        }
                    }
            )
            TextField(
                value = value,
                singleLine = true,
                onValueChange = { value = it },
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
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
                LocalContext.current.setWindowAlpha(0.6f, true)
                // BottomNavigation height
                val offsetY = with(LocalDensity.current) { 56.dp.toPx().toInt() }
                Popup(
                    popupPositionProvider = PopupBottomPositionProvider(IntOffset(0, offsetY)),
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

            } else {
                LocalContext.current.setWindowAlpha(1f, true)
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
                LoadingDialog(text = "?????????", onDismissRequest = { openLoading.value = false })
            }

            val scope = rememberCoroutineScope()
            Button(onClick = {
                scope.launch {
                    User.isLogin(false)
                }
                controller.navigate("login") {
                    // ????????? login ????????????????????? main????????? main
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
        // ?????????????????????background
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
                Text(text = "??????")
            }
            Tab(
                selected = selectedIndex.value == 1,
                modifier = Modifier.fillMaxHeight(),
                unselectedContentColor = unSelectedColor,
                selectedContentColor = selectedColor,
                onClick = { selectedIndex.value = 1 }) {
                Text(text = "??????")
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


