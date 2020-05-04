package com.example.webnovelreader.chapters

import com.example.webnovelreader.interfaces.Chapter

class BoxNovelChapter(
    chapterNum: Double,
    chapterTitle: String,
    content: String,
    url: String,
    nextChapter: String = "",
    prevChapter: String = ""
) : Chapter {

    override val chapterNum: Double = chapterNum
    override var chapterTitle: String = chapterTitle
        set(chapterTitle) {this.chapterTitle = chapterTitle}
    override var content: String = content
        set(content) {this.content = content}
    override var url: String = url
        set(url) {this.url = url}
    override var nextChapter: String = nextChapter
        set(nextChapter) {this.nextChapter = nextChapter}
    override var prevChapter: String = prevChapter
        set(prevChapter) {this.prevChapter = prevChapter}

}