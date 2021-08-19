package com.github.crayonxiaoxin.wanandroid.ui.mine

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
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
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import java.io.File

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

private fun Context.saveMediaToStorageUri(): Pair<String, Uri?> {
    val currentFilePath: String
    //Generating a file name
    val filename = "IMG_${System.currentTimeMillis()}.jpeg"
    //Output stream
    var imageUri: Uri? = null
    //For devices running android >= Q
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //getting the contentResolver
        contentResolver?.also { resolver ->
            //Content resolver will process the contentvalues
            val contentValues = ContentValues().apply {
                //putting file information in content values
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            //Inserting the contentValues to contentResolver and getting the Uri
            imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        }
        currentFilePath = ""
    } else {
        //These for devices running on android < Q
        //So I don't think an explanation is needed here
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        currentFilePath = image.absolutePath
        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                this,
                "$packageName.provider",
                image
            )
        } else {
            Uri.fromFile(image)
        }
    }
    return Pair(currentFilePath, imageUri)
}

