package com.kdownloader.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS " +
                    AppDbHelper.TABLE_NAME.toString() + "( " +
                    DownloadModel.ID + " INTEGER PRIMARY KEY, " +
                    DownloadModel.URL + " VARCHAR, " +
                    DownloadModel.ETAG + " VARCHAR, " +
                    DownloadModel.DIR_PATH + " VARCHAR, " +
                    DownloadModel.FILE_NAME + " VARCHAR, " +
                    DownloadModel.TOTAL_BYTES + " INTEGER, " +
                    DownloadModel.DOWNLOADED_BYTES + " INTEGER, " +
                    DownloadModel.LAST_MODIFIED_AT + " INTEGER " +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {}

    companion object {
        private const val DATABASE_NAME = "kdownloader.db"
        private const val DATABASE_VERSION = 1
    }
}