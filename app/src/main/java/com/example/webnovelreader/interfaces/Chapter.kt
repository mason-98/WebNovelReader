package com.example.webnovelreader.interfaces

interface Chapter {
    val chapterNum: Double
    var chapterTitle: String
    var nextChapter: Chapter
    var prevChapter: Chapter
    var content: String
    var book: Book
    var ext: String

    fun getChapterNum()
    fun getChapterTitle()
    fun setChapterTitle()
    fun getNextChapter()
    fun setNextChapter()
    fun getPrevChapter()
    fun getContent()
    fun setContent()

}