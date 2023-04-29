package com.kdownloader.database

interface DbHelper {

    suspend fun find(id: Int): DownloadModel?

    suspend fun insert(model: DownloadModel)

    suspend fun update(model: DownloadModel)

    suspend fun updateProgress(id: Int, downloadedBytes: Long, lastModifiedAt: Long)

    suspend fun remove(id: Int)

    suspend fun getUnwantedModels(days: Int): List<DownloadModel>?

    suspend fun empty()

}