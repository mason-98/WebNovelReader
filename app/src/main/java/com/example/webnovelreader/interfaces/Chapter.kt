package com.example.webnovelreader.interfaces

interface Chapter {
    val chapterNum: Double
    var chapterTitle: String
    var content: String
    var url: String
    var nextChapter: String
    var prevChapter: String

}