package com.kdownloader.database

class NoOpsDbHelper : DbHelper {

    override suspend fun find(id: Int): DownloadModel? {
        return null
    }

    override suspend fun insert(model: DownloadModel) {
    }

    override suspend fun update(model: DownloadModel) {
    }

    override suspend fun updateProgress(id: Int, downloadedBytes: Long, lastModifiedAt: Long) {
    }

    override suspend fun remove(id: Int) {
    }

    override suspend fun getUnwantedModels(days: Int): List<DownloadModel>? {
        return null
    }

    override suspend fun empty() {

    }
}