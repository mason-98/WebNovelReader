package com.example.webnovelreader.books.covers

import com.example.webnovelreader.interfaces.BookCover

class NovelAllBookCover(bookCoverUrl :String,
                        bookTitle: String,
                        bookUrl: String) : BookCover {
    override var bookCoverUrl: String = bookCoverUrl
        set(value) {this.bookCoverUrl = value}
    override var bookTitle: String = bookTitle
        set(value) {this.bookTitle = value}
    override var bookUrl: String = bookUrl
        set(value) {this.bookUrl = value}
}