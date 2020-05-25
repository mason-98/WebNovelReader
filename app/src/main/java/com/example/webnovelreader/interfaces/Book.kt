package com.example.webnovelreader.interfaces

import java.time.LocalDate

interface Book {
    var chapterList: List<Chapter>
    val title: String
    val author: String
    var lastReadChapter: Chapter
    var lastOpened: LocalDate
    var url: String
    var source: String
}