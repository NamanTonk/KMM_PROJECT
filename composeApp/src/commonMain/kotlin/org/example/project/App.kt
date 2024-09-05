package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.collectLatest


@Composable
fun App() {
    var count by mutableStateOf(0)
    val imagesList = mutableStateListOf<Photo>()
   LaunchedEffect(Unit){
       getPhoto().fetchPhotos().collectLatest {
           imagesList.clear()
           imagesList.addAll(it)
       }
   }
    MaterialTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
Text("Photo Gallery")
            }, contentColor = TopAppBarDefaults.smallTopAppBarColors( )
        }) { padding->
            Column (Modifier.padding(padding)){

            }
        }
    }
}