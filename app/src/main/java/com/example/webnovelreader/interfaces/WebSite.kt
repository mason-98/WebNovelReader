package com.example.webnovelreader.interfaces

interface WebSite {
    val baseURL: String
    val latestUpdateExt: String


    fun scrapeLatestUpdates()
    fun scrapeBook(book: Book)
    fun scrapeChapter(chapter: Chapter)
}