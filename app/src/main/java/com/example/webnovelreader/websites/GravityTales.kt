package com.example.webnovelreader.websites

import android.os.AsyncTask
import android.util.Log.d
import android.view.Gravity
import com.example.webnovelreader.books.GravityTalesBook
import com.example.webnovelreader.interfaces.BookCover
import com.example.webnovelreader.chapters.GravityTalesChapter
import com.example.webnovelreader.interfaces.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.lang.IndexOutOfBoundsException

class GravityTales (override val baseURL: String = "https://gravitytales.com/",
                   override val latestUpdateExt: String = "updates?page=") : WebSite {

    private class JSoupGetUrl : AsyncTask<String, Void, Document>() {
        override fun doInBackground(vararg params: String): Document {
            return Jsoup.connect(params[0]).maxBodySize(0).get()
        }

    }

    override fun scrapeBook(bookUrl: String): GravityTalesBook {
            val doc = JSoupGetUrl().execute(bookUrl).get()
            val bookTitleContainer = doc.select("div.main-content")
            var bookTitle: String
            val chapterList = scrapeChapterList(bookUrl)
            bookTitle = bookTitleContainer.select("h3")[0].childNode(0).toString()
            var author: String
            val descContainer = doc.select("div.main-content div.desc p")[2]
            author = descContainer.childNode(1).toString().replace("&nbsp;", "")
            return GravityTalesBook(bookTitle, bookUrl, author, chapterList)
    }


    override fun scrapeChapterList(bookUrl: String): List<Chapter> {
            val doc = JSoupGetUrl().execute(bookUrl).get()
            val chapterListHTML = doc.select("div#chapters")
            return chapterListHTML.map {
                val chapterTitle = it.childNode(0).toString()
                val chapterURL = it.childNode(0).attr("href").toString()
                var chapter = GravityTalesChapter(chapterTitle, "")
                chapter
            }
    }


        override fun scrapeLatestUpdates(page: String): List<BookCover> {
            return emptyList()
        }

        override fun scrapeChapter(chapterURL: String, chapterTitle: String): Chapter {
            return GravityTalesChapter("", "", "", "", "")
        }

    }