package com.example.webnovelreader.books.covers

import com.example.webnovelreader.interfaces.BookCover
import com.example.webnovelreader.interfaces.WebSite
import com.example.webnovelreader.websites.BoxNovel

class BoxNovelBookCover(bookCoverUrl :String,
                        bookTitle: String,
                        bookUrl: String) : BookCover {
    override var bookCoverUrl: String = bookCoverUrl
        set(value) {this.bookCoverUrl = value}
    override var bookTitle: String = bookTitle
        set(value) {this.bookTitle = value}
    override var bookUrl: String = bookUrl
        set(value) {this.bookUrl = value}
    override var source: WebSite = BoxNovel()
}