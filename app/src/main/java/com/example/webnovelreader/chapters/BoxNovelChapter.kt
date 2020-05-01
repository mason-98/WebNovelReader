package com.example.webnovelreader.chapters

import com.example.webnovelreader.interfaces.Book
import com.example.webnovelreader.interfaces.Chapter

class BoxNovelChapter(override val chapterNum: Double,
                      override var chapterTitle: String,
                      override var content: String,
                      override var url: String,
                      override var nextChapter: String = "",
                      override var prevChapter: String = ""
) : Chapter {



    override fun getChapterNum() {
        TODO("Not yet implemented")
    }

    override fun getContent() {
        TODO("Not yet implemented")
    }

    override fun setContent() {
        TODO("Not yet implemented")
    }

    override fun getChapterTitle() {
        TODO("Not yet implemented")
    }

    override fun setChapterTitle() {
        TODO("Not yet implemented")
    }

    override fun getNextChapter() {
        TODO("Not yet implemented")
    }

    override fun getPrevChapter() {
        TODO("Not yet implemented")
    }

}