package com.example.webnovelreader.websites

import android.os.AsyncTask
import android.util.Log.d
import com.example.webnovelreader.books.BoxNovelBook
import com.example.webnovelreader.books.covers.BoxNovelBookCover
import com.example.webnovelreader.chapters.BoxNovelChapter
import com.example.webnovelreader.interfaces.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.Serializable
import java.lang.Exception

class BoxNovel(override val baseURL: String = "https://boxnovel.com/",
               override val latestUpdateExt: String = "page/") : WebSite, Serializable {

    private class JSoupGetUrl : AsyncTask<String, Void, Document>(){
        override fun doInBackground(vararg params: String): Document {
            return Jsoup.connect(params[0]).maxBodySize(0).get()
        }

    }

    override fun scrapeBook(bookUrl: String): BoxNovelBook {
        try {
            val doc = JSoupGetUrl().execute(bookUrl).get()
            val bookTitleHeader = doc.select("div.post-title h3")[0]
            val bookTitle: String
            val chapterList = scrapeChapterList(bookUrl)
            bookTitle = try {
                bookTitleHeader.childNode(2).toString().trim()
            } catch (e: Exception) {
                bookTitleHeader.childNode(0).toString().trim()
            }
            val author: String
            author = doc.selectFirst("div.author-content a").text()
            return BoxNovelBook(bookTitle, bookUrl, author, chapterList)
        } catch(e: Exception){
            d("Error", e.toString())
            return BoxNovelBook("", "", "", emptyList())
        }
    }

    override fun scrapeChapterList(bookUrl: String): List<Chapter> {
        return try {
            val doc = JSoupGetUrl().execute(bookUrl).get()
            val chapterListHTML = doc.select("li.wp-manga-chapter")
            chapterListHTML.map {
                val chapterTitle = it.child(0).text().toString()
                val chapterURL = it.child(0).attr("href").toString()
                val chapter = BoxNovelChapter(chapterTitle, chapterURL)
                chapter
            }
        } catch (e: Exception){
            d("Error", e.toString())
            emptyList()
        }
    }

    override fun scrapeLatestUpdates(page: String) : List<BookCover> {
        try{
            val url = baseURL + latestUpdateExt + page
            val bookCovers: List<BookCover>
            val doc = JSoupGetUrl().execute(url).get()
            val bookCoversHtml = doc.select("div.page-content-listing div.page-item-detail")
            bookCovers = bookCoversHtml.map {
                BoxNovelBookCover(
                    it.selectFirst("a img").attr("src"),
                    it.selectFirst("a").attr("title"),
                    it.selectFirst("a").attr("href")
                )
            }
            return bookCovers
        } catch (e:Exception){
            d("Error", e.toString())
            return emptyList()
        }
    }

    override fun scrapeChapter(chapterURL: String, chapterTitle: String) : BoxNovelChapter {
        try {
            val doc = JSoupGetUrl().execute(chapterURL).get()
            val contentHtml = doc.selectFirst("div.text-left").select("p")
            var nextChapter = ""
            var prevChapter = ""
            val nextChapterHTML = doc.selectFirst("div.nav-next a")
            nextChapterHTML?.let {
                nextChapter = it.attr("href").toString()
            }
            var prevChapterHTML = doc.selectFirst("div.nav-previous a")
            prevChapterHTML?.let {
                prevChapter = it.attr("href").toString()
            }
            val content =
                if (contentHtml[0].toString().contains("<strong>")){
                    contentHtml.drop(1).joinToString(separator = "").replace("<p>&nbsp;</p>", "").replace(
                        "<p>", ""
                    ).replace("</p>", "\n\n")
                } else {
                    contentHtml.joinToString(separator = "").replace("<p>&nbsp;</p>", "").replace(
                        "<p>", ""
                    ).replace("</p>", "\n\n")
                }

            val newChapterTitle: String
            newChapterTitle = if (chapterTitle == "") {
                doc.select("li.active").text().toString()
            } else {
                chapterTitle
            }
            return BoxNovelChapter(newChapterTitle, chapterURL, content, nextChapter, prevChapter)
        } catch (e: Exception){
            d("Error", e.toString())
            return BoxNovelChapter("", "", "", "", "")
        }

    }
}