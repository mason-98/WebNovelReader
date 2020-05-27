package com.example.webnovelreader.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "WebNovelDatabase.db"
        private const val SQL_CREATE_TABLES = "CREATE TABLE Book (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "book_name TEXT NOT NULL," +
                "book_source TEXT," +
                "book_url TEXT NOT NULL," +
                "bookmarked BOOLEAN NOT NULL CHECK (bookmarked in (0,1))," +
                "book_image_source TEXT," +
                "last_opened TEXT);"
        private const val SQL_DELETE_TABLES = "DROP TABLE IF EXISTS Book"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLES)
        onCreate(db)
    }

}