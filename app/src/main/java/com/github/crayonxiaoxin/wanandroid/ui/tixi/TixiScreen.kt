package com.github.crayonxiaoxin.wanandroid.ui.tixi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.toast
import com.google.accompanist.insets.statusBarsPadding

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
        Column {
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