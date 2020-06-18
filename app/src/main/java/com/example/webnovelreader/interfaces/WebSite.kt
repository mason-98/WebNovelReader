package com.example.webnovelreader.interfaces

import android.content.Context
import com.example.webnovelreader.books.BoxNovelBook
import java.io.Serializable

interface WebSite : Serializable {
    val baseURL: String
    val latestUpdateExt: String
    val directoryExt: String
    val sourceName: String


    fun scrapeLatestUpdates(page: String, context:Context, allNovel:Boolean) : List<BookCover>
    fun scrapeBook(bookUrl: String): Book
    fun scrapeChapter(chapterURL: String, chapterTitle: String = "", bookTitle: String = "") : Chapter
    fun scrapeChapterList(bookUrl: String): List<Chapter>
}