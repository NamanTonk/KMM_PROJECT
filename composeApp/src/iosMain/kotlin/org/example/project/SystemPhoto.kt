package org.example.project

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSSortDescriptor
import platform.Foundation.timeIntervalSince1970
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHFetchOptions
import platform.Photos.PHPhotoLibrary

class PhotosIos : PhotoFetcher {
    @OptIn(ExperimentalForeignApi::class, ExperimentalCoroutinesApi::class)
    override suspend fun fetchPhotos(): StateFlow<List<Photo>> {
        val flowData = MutableStateFlow<List<Photo>>(emptyList())
        return suspendCancellableCoroutine { continuation ->
            val photos = mutableListOf<Photo>()
            PHPhotoLibrary.requestAuthorization { status ->
                if (status == PHAuthorizationStatusAuthorized) {
                    val fetchOptions = PHFetchOptions()
                    fetchOptions.sortDescriptors = listOf(
                        NSSortDescriptor(key = "creationDate", ascending = false)
                    )
                    val fetchResult =
                        PHAsset.fetchAssetsWithMediaType(PHAssetMediaTypeImage, fetchOptions)

                    fetchResult.enumerateObjectsUsingBlock { asset, _, _ ->
                        if (asset is PHAsset) {
                            val date = asset.creationDate?.timeIntervalSince1970?.toLong() ?: 0L
                            val name = asset.localIdentifier
                            photos.add(Photo(uri = name, name = name, date = date))
                        }
                    }
                    flowData.update { photos }
                    continuation.resume(flowData.asStateFlow()) {}
                } else {
                    continuation.resume(flowData.asStateFlow()) {}
                }
            }
        }
    }
}

actual fun getPhoto(): PhotoFetcher = PhotosIos()
