package com.kdownloader.database


data class DownloadModel(

    var id: Int = 0,
    var url: String = "",
    var eTag: String = "",
    var dirPath: String = "",
    var fileName: String = "",
    var totalBytes: Long = 0,
    var downloadedBytes: Long = 0,
    var lastModifiedAt: Long = 0
) {

    companion object {
        const val ID = "id"
        const val URL = "url"
        const val ETAG = "etag"
        const val DIR_PATH = "dir_path"
        const val FILE_NAME = "file_name"
        const val TOTAL_BYTES = "total_bytes"
        const val DOWNLOADED_BYTES = "downloaded_bytes"
        const val LAST_MODIFIED_AT = "last_modified_at"
    }
}