package com.example.webnovelreader.websitesimport
import android.os.AsyncTask
import android.util.Log.d
import com.example.webnovelreader.books.VeraTalesBook
import com.example.webnovelreader.chapters.VeraTalesChapter
import com.example.webnovelreader.interfaces.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception


class VeraTales (override val baseURL: String = "https://veratales.com/",
                    override val latestUpdateExt: String = "?page") : WebSite {

    private class JSoupGetUrl : AsyncTask<String, Void, Document>(){
        override fun doInBackground(vararg params: String): Document {
            return Jsoup.connect(params[0]).maxBodySize(0).get()
        }

    }

    override fun scrapeBook(bookUrl: String): VeraTalesBook {
        val doc = JSoupGetUrl().execute(bookUrl).get()
        val bookTitleHeader = doc.select("div.row div.col-md-8 h1")[0]
        var bookTitle: String
        bookTitle = bookTitleHeader.childNode(0).toString()
        d("nick",bookTitle)
        return VeraTalesBook("","","", emptyList())
    }

    override fun scrapeChapterList(bookUrl: String): List<Chapter> {
        return emptyList()
    }

    override fun scrapeLatestUpdates(page: String) : List<BookCover> {
        return emptyList()
    }

    override fun scrapeChapter(chapterURL: String, chapterTitle: String) : VeraTalesChapter {
        return VeraTalesChapter("","","","","")
    }
}