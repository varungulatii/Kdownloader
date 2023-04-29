package com.kdownloader.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppDbHelper(context: Context?) : DbHelper {

    private var db: SQLiteDatabase

    companion object {
        const val TABLE_NAME = "downloads"
    }

    init {
        val databaseOpenHelper = DatabaseOpenHelper(context)
        db = databaseOpenHelper.writableDatabase
    }

    @SuppressLint("Range")
    override suspend fun find(id: Int): DownloadModel? {
        var downloadModel: DownloadModel? = null
        val cursor = db.rawQuery(
            "SELECT * FROM " + TABLE_NAME + " WHERE " + DownloadModel.ID + " = " + id,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            downloadModel = DownloadModel(
                id = id,
                url = cursor.getString(cursor.getColumnIndex(DownloadModel.URL)),
                eTag = cursor.getString(cursor.getColumnIndex(DownloadModel.ETAG)),
                dirPath = cursor.getString(cursor.getColumnIndex(DownloadModel.DIR_PATH)),
                fileName = cursor.getString(cursor.getColumnIndex(DownloadModel.FILE_NAME)),
                totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadModel.TOTAL_BYTES)),
                downloadedBytes = cursor.getLong(cursor.getColumnIndex(DownloadModel.DOWNLOADED_BYTES)),
                lastModifiedAt = cursor.getLong(cursor.getColumnIndex(DownloadModel.LAST_MODIFIED_AT))
            )
        }
        cursor.close()
        return downloadModel
    }

    override suspend fun insert(model: DownloadModel) {
        val values = ContentValues()
        values.put(DownloadModel.ID, model.id)
        values.put(DownloadModel.URL, model.url)
        values.put(DownloadModel.ETAG, model.eTag)
        values.put(DownloadModel.DIR_PATH, model.dirPath)
        values.put(DownloadModel.FILE_NAME, model.fileName)
        values.put(DownloadModel.TOTAL_BYTES, model.totalBytes)
        values.put(DownloadModel.DOWNLOADED_BYTES, model.downloadedBytes)
        values.put(DownloadModel.LAST_MODIFIED_AT, model.lastModifiedAt)
        db.insert(TABLE_NAME, null, values)
    }

    override suspend fun update(model: DownloadModel) {
        try {
            val values = ContentValues()
            values.put(DownloadModel.URL, model.url)
            values.put(DownloadModel.ETAG, model.eTag)
            values.put(DownloadModel.DIR_PATH, model.dirPath)
            values.put(DownloadModel.FILE_NAME, model.fileName)
            values.put(DownloadModel.TOTAL_BYTES, model.totalBytes)
            values.put(DownloadModel.DOWNLOADED_BYTES, model.downloadedBytes)
            values.put(DownloadModel.LAST_MODIFIED_AT, model.lastModifiedAt)
            db.update(
                TABLE_NAME,
                values,
                DownloadModel.ID + " = ? ",
                arrayOf((model.id).toString())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateProgress(id: Int, downloadedBytes: Long, lastModifiedAt: Long) {
        withContext(Dispatchers.IO) {
            try {
                val values = ContentValues()
                values.put(DownloadModel.DOWNLOADED_BYTES, downloadedBytes)
                values.put(DownloadModel.LAST_MODIFIED_AT, lastModifiedAt)
                db.update(
                    TABLE_NAME,
                    values,
                    DownloadModel.ID + " = ? ",
                    arrayOf("$id")
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun remove(id: Int) {
        try {
            db.execSQL(
                "DELETE FROM " + TABLE_NAME + " WHERE " +
                        DownloadModel.ID + " = " + id
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("Range")
    override suspend fun getUnwantedModels(days: Int): List<DownloadModel> {
        val models: MutableList<DownloadModel> = ArrayList()
        var cursor: Cursor? = null
        try {
            val daysInMillis = days * 24 * 60 * 60 * 1000L
            val beforeTimeInMillis = System.currentTimeMillis() - daysInMillis
            cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " +
                        DownloadModel.LAST_MODIFIED_AT + " <= " + beforeTimeInMillis, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val model = DownloadModel()
                    model.id = (cursor.getInt(cursor.getColumnIndex(DownloadModel.ID)))
                    model.url = (cursor.getString(cursor.getColumnIndex(DownloadModel.URL)))
                    model.eTag = (cursor.getString(cursor.getColumnIndex(DownloadModel.ETAG)))
                    model.dirPath =
                        (cursor.getString(cursor.getColumnIndex(DownloadModel.DIR_PATH)))
                    model.fileName =
                        (cursor.getString(cursor.getColumnIndex(DownloadModel.FILE_NAME)))
                    model.totalBytes =
                        (cursor.getLong(cursor.getColumnIndex(DownloadModel.TOTAL_BYTES)))
                    model.downloadedBytes =
                        (cursor.getLong(cursor.getColumnIndex(DownloadModel.DOWNLOADED_BYTES)))
                    model.lastModifiedAt =
                        (cursor.getLong(cursor.getColumnIndex(DownloadModel.LAST_MODIFIED_AT)))
                    models.add(model)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return models
    }

    override suspend fun empty() {
        try {
            db.execSQL("DELETE * FROM " + TABLE_NAME)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}