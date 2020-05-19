package com.example.webnovelreader.websitesimport
import android.os.AsyncTask
import android.util.Log.d
import com.example.webnovelreader.books.NovelAllBook
import com.example.webnovelreader.chapters.NovelAllChapter
import com.example.webnovelreader.interfaces.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class NovelAll (override val baseURL: String = "https://veratales.com/",
                override val latestUpdateExt: String = "?page") : WebSite {

    private class JSoupGetUrl : AsyncTask<String, Void, Document>(){
        override fun doInBackground(vararg params: String): Document {
            return Jsoup.connect(params[0]).maxBodySize(0).get()
        }

    }

    override fun scrapeBook(bookUrl: String): NovelAllBook {
        val doc = JSoupGetUrl().execute(bookUrl).get()
        val bookTitleHeader = doc.select("div.manga-detail h1")[0]
        var bookTitle: String
        bookTitle = bookTitleHeader.childNode(0).toString().replace("amp;", "")
        d("nick",bookTitle)
        val author: String
        author = doc.select("div.manga-detail div.detail-info p a")[0].childNode(0).toString()
        d("nick", author)
        val chapterList = scrapeChapterList(bookUrl)
        return NovelAllBook(bookTitle,bookUrl,author, chapterList)
    }

    override fun scrapeChapterList(bookUrl: String): List<Chapter> {
        return try {
            val doc = JSoupGetUrl().execute(bookUrl).get()
            val chapterListHTML = doc.select("div.manga-detailchapter li")
            chapterListHTML.map {
                val chapterTitle = it.child(0).attr("title").toString()
                val chapterURL = it.child(0).attr("href").toString()
                val chapter = NovelAllChapter(chapterTitle, chapterURL)
                chapter
            }
        } catch (e: Exception){
            d("Error", e.toString())
            emptyList()
        }
    }

    override fun scrapeLatestUpdates(page: String) : List<BookCover> {
        return emptyList()
    }

    override fun scrapeChapter(chapterURL: String, chapterTitle: String) : NovelAllChapter {
        return NovelAllChapter("","","","","")
    }
}