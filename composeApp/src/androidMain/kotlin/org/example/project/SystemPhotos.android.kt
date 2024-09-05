package org.example.project

import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine


class PhotoAndroid : PhotoFetcher {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun fetchPhotos(): StateFlow<List<Photo>> {
        val updateFlow = MutableStateFlow<List<Photo>> (emptyList())
       return suspendCancellableCoroutine {coroutine->
           val list = mutableListOf<Uri>()
           try {
               val dataIndex: Int
               val projection =
                   arrayOf(MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.Images.Media._ID)
               AppApplication.instance.contentResolver?.query(
                   MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                   projection,
                   null,
                   null,
                   "${MediaStore.Images.Media.DATE_ADDED} DESC"
               )?.use { cursor ->
                   dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                   while (cursor.moveToNext()) {
                       println(cursor.getLong(dataIndex).toString())
                       val bitmap = Uri.withAppendedPath(
                           MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                           cursor.getLong(dataIndex).toString()
                       )
                       list.add(bitmap)
                   }

               }
                updateFlow.update {  list.map { Photo(it.toString(), "", 0)} }
               coroutine.resume(updateFlow.asStateFlow()){}
           } catch (e: Exception) {
               e.printStackTrace()
                   coroutine.resume(updateFlow.asStateFlow()){}
           }

       }

    }
}

actual fun getPhoto(): PhotoFetcher = PhotoAndroid()
