package com.github.crayonxiaoxin.wanandroid.ui.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(controller: NavHostController) {
    Scaffold {
        LoginBackground()
        LoginForm(controller)
    }
}

@Composable
private fun LoginBackground() {
    Image(
        painter = painterResource(id = R.drawable.bg_jetpack),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.1f),
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.TopEnd
    )
    Image(
        painter = painterResource(id = R.drawable.bg_compose),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.1f),
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.BottomStart
    )
}

@ExperimentalAnimationApi
@Composable
private fun LoginForm(controller: NavHostController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(32.dp)
    ) {
        val (logo, input1, input2, button) = createRefs()
        createVerticalChain(input1, input2, button, chainStyle = ChainStyle.Packed)

        Image(
            painter = painterResource(id = R.drawable.wanandroid_logo),
            contentDescription = "logo",
            modifier = Modifier
                .constrainAs(logo) {
                    bottom.linkTo(input1.top)
                    centerHorizontallyTo(parent)
                }
                .width(200.dp)
        )

        val usernameState = remember { UsernameFieldState() }
        val passwordState = remember { PasswordFieldState() }
        val isLogin = remember { mutableStateOf(false) }
        UserNameField(
            "用户名",
            usernameState,
            modifier = Modifier
                .constrainAs(input1) {
                    top.linkTo(parent.top, margin = 8.dp)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth(),
            enabled = !isLogin.value
        )
        PasswordField(
            "密码",
            passwordState,
            modifier = Modifier
                .constrainAs(input2) {
                    top.linkTo(input1.bottom, margin = 8.dp)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth(),
            enabled = !isLogin.value
        )
        Button(
            onClick = {
                isLogin.value = true
                Log.e("LoginScreen", "LoginForm: " + passwordState.text)
                //                controller.navigate("home")
            },
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(input2.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth(),
            enabled = usernameState.isValid && passwordState.isValid && !isLogin.value
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val rotate by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        180f at 500
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(visible = isLogin.value) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Login",
                        modifier = Modifier
                            .height(32.dp)
                            .rotate(rotate)
                    )
                }
                Text("Login", modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
private fun UserNameField(
    label: String,
    usernameState: UsernameFieldState,
    modifier: Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = usernameState.text,
            onValueChange = {
                usernameState.text = it
                usernameState.enableShowErrors()
            },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    val focused = it == FocusState.Active
                    usernameState.onFocusChange(focused)
                    if (!focused) {
                        usernameState.enableShowErrors()
                    }
                },
            isError = usernameState.showErrors(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeAction()
                }
            ),
            enabled = enabled
        )
        usernameState.getError()?.let {
            TextFieldError(textError = it)
        }
    }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}

@Composable
private fun PasswordField(
    label: String,
    passwordState: PasswordFieldState,
    modifier: Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
    ) {
        val showPassword = remember { mutableStateOf(false) }
        OutlinedTextField(
            value = passwordState.text,
            onValueChange = {
                passwordState.text = it
                passwordState.enableShowErrors()
            },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    val focused = it == FocusState.Active
                    passwordState.onFocusChange(focused)
                    if (!focused) {
                        passwordState.enableShowErrors()
                    }
                },
            isError = passwordState.showErrors(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onDone = {
                    onImeAction()
                }
            ),
            trailingIcon = {
                if (showPassword.value) {
                    IconButton(onClick = { showPassword.value = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "Hide Password"
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "Show Password"
                        )
                    }
                }
            },
            visualTransformation = if (showPassword.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            enabled = enabled
        )
        passwordState.getError()?.let {
            TextFieldError(textError = it)
        }
    }
}