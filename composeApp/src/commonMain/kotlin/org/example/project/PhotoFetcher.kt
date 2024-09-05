package org.example.project

import kotlinx.coroutines.flow.StateFlow

interface PhotoFetcher {
    suspend fun fetchPhotos():StateFlow<List<Photo>>
}
// Photo Model
data class Photo(val uri:String, val name:String,val date:Long)

expect fun getPhoto():PhotoFetcher

