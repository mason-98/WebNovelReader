package com.example.webnovelreader.interfaces

import java.time.LocalDate

interface Book {
    var chapterList: List<Chapter>
    val title: String
    val author: String
    var genres: List<String>
    var lastReadChapter: Chapter
    var lastOpened: LocalDate
    val source: WebSite
    var ext: String

    fun getLastReadChapter()
    fun setLastReadChapter()
    fun getTitle()
    fun getAuthor()
    fun setGenres()
    fun getGenres()
    fun getSource()
}