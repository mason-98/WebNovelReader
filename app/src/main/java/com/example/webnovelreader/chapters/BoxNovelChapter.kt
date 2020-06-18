package com.example.webnovelreader.chapters

import com.example.webnovelreader.interfaces.Chapter

class BoxNovelChapter(bookTitle: String,
    chapterTitle: String,
    url: String,
    content: String = "",
    nextChapter: String = "",
    prevChapter: String = ""
) : Chapter {
    override var bookTitle: String = bookTitle
        set(bookTitle) {this.bookTitle = bookTitle}
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