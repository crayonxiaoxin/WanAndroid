package com.github.crayonxiaoxin.wanandroid.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.github.crayonxiaoxin.wanandroid.R

@Composable
fun LoginScreen(controller: NavHostController) {
    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("LoginScreen") }, navigationIcon = {
//                Image(
//                    painter = painterResource(id = R.drawable.wanandroid_logo),
//                    contentDescription = ""
//                )
//            })
//        }
    ) {
        LoginForm(controller)
    }
}

@Composable
private fun LoginForm(controller: NavHostController) {
    val (username, setUsername) = remember(calculation = { mutableStateOf("") })
    val (password, setPassword) = remember(calculation = { mutableStateOf("") })
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
        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            label = { Text("Username") },
            modifier = Modifier
                .constrainAs(input1) {
                    top.linkTo(parent.top, margin = 8.dp)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password") },
            modifier = Modifier
                .constrainAs(input2) {
                    top.linkTo(input1.bottom, margin = 8.dp)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onDone = {}
            )
        )
        Button(
            onClick = {
//                controller.navigate("home")
            },
            modifier = Modifier
                .constrainAs(button) {
                    top.linkTo(input2.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth()
        ) {
            Text("Login", modifier = Modifier.padding(4.dp))
        }
    }
}