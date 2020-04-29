package com.example.webnovelreader.websites

import com.example.webnovelreader.interfaces.*

class BoxNovel(override val baseURL: String, override val latestUpdateExt: String) : WebSite {

    override fun scrapeBook(book: Book) {
        TODO("Not yet implemented")
    }

    override fun scrapeLatestUpdates() {
        TODO("Not yet implemented")
    }

    override fun scrapeChapter(chapter: Chapter) {
        TODO("Not yet implemented")
    }
}