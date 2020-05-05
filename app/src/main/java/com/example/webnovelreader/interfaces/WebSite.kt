package com.example.webnovelreader.interfaces

import com.example.webnovelreader.books.BoxNovelBook

interface WebSite {
    val baseURL: String
    val latestUpdateExt: String


    fun scrapeLatestUpdates(page: String) : List<BookCover>
    fun scrapeBook(bookUrl: String): Book
    fun scrapeChapter(chapterURL: String, chapterTitle: String = "") : Chapter
    fun scrapeChapterList(bookUrl: String): List<Chapter>
}