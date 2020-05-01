package com.example.webnovelreader.interfaces

interface WebSite {
    val baseURL: String
    val latestUpdateExt: String


    fun scrapeLatestUpdates()
    fun scrapeBook(bookExt: String)
    fun scrapeChapter(chapterURL: String, chapterNum: Double, chapterTitle: String) : Chapter
}