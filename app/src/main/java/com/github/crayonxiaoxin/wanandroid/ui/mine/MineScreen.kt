package com.github.crayonxiaoxin.wanandroid.ui.mine

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.util.saveMediaToStorageUri

@Composable
fun MineScreen(controller: NavHostController) {
//    var state by remember {
//        mutableStateOf<PageState>(PageState.Loading)
//    }
//    LaunchedEffect(key1 = 1) {
//        delay(3000)
//        state = PageState.Empty
//        delay(3000)
//        state = PageState.Retry
//    }
//    val scope = rememberCoroutineScope()
//    PageStateLayout(
//        state = state,
//        retryOnClick = {
//            scope.launch {
//                state = PageState.Loading
//                delay(3000)
//                state = PageState.Content
//            }
//        }
//    ) {
//        DefaultEmptyView("Content")
//    }
    PickPhotoDemo()
}

@Composable
fun PickPhotoDemo() {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val filePath = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val pickPhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }
    val pair = remember {
        mutableStateOf<Pair<String, Uri?>>(Pair("", null))
    }
    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            filePath.value = pair.value.first
            imageUri = pair.value.second
        }
    }

    Column {
        Spacer(modifier = Modifier.height(60.dp))
        Row {
            Button(onClick = {
                pickPhoto.launch("image/*")
            }) {
                Text(text = "Pick image")
            }
            Button(onClick = {
                pair.value = context.saveMediaToStorageUri().apply {
                    takePhoto.launch(this.second)
                }
            }) {
                Text(text = "Take picture")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            bitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(400.dp)
                )
            }
        }
    }
}



