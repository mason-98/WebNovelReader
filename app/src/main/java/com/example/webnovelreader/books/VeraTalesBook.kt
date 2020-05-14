package com.example.webnovelreader.books
import com.example.webnovelreader.interfaces.Book
import com.example.webnovelreader.interfaces.Chapter
import com.example.webnovelreader.interfaces.WebSite
import java.time.LocalDate


class VeraTalesBook (  title: String,
                       url: String,
                       author: String,
                       chapterList: List<Chapter>) : Book{

        override var chapterList: List<Chapter> = chapterList
            set(chapterList) {this.chapterList = chapterList}
        override val title: String = title
        override val author: String = author
        override var lastReadChapter: Chapter
            get() = this.lastReadChapter
            set(value) {this.lastReadChapter = value}
        override var lastOpened: LocalDate
            get() = this.lastOpened
            set(value) {this.lastOpened = value}
        override var url: String = url
            set(url) {this.url=url}


    }