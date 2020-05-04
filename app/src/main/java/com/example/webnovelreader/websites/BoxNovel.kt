package com.example.webnovelreader.websites

import android.util.Log.d
import com.example.webnovelreader.books.BoxNovelBook
import com.example.webnovelreader.chapters.BoxNovelChapter
import com.example.webnovelreader.interfaces.*
import org.jsoup.Jsoup
import java.lang.Exception

class BoxNovel(override val baseURL: String = "https://boxnovel.com/",
               override val latestUpdateExt: String = "page/") : WebSite {

    override fun scrapeBook(bookUrl: String): BoxNovelBook {
        val doc = Jsoup.connect(bookUrl).maxBodySize(0).get()
        val bookTitleHeader = doc.select("div.post-title h3")[0]
        var bookTitle: String
        val chapterListHTML = doc.select("li.wp-manga-chapter")
        val chapterList = scrapeChapterList(bookUrl)
        bookTitle = try {
            bookTitleHeader.childNode(2).toString().trim()
        } catch (e: Exception){
            bookTitleHeader.childNode(0).toString().trim()
        }
        var author: String
        author = doc.selectFirst("div.author-content a").text()
        return BoxNovelBook(bookTitle, bookUrl, author, chapterList)
    }

    override fun scrapeChapterList(bookUrl: String): Map<String, Chapter> {
        val doc = Jsoup.connect(bookUrl).maxBodySize(0).get()
        val chapterListHTML = doc.select("li.wp-manga-chapter")
        return chapterListHTML.map {
            val chapterInfo = it.child(0).text().toString().removePrefix(
                "Chapter "
            ).split(" - ", limit = 2)
            val chapterURL = it.child(0).attr("href").toString()
            var chapter = try {
                scrapeChapter(
                    chapterURL,
                    chapterInfo[0].toDouble(), chapterInfo[1]
                )
            } catch (e: IndexOutOfBoundsException){
                scrapeChapter(
                    chapterURL,
                    chapterInfo[0].toDouble(), ""
                )
            }
            chapter
        }.associateBy( {it.url}, {it} )
    }

    override fun scrapeLatestUpdates() : List<BookCover> {
        TODO("Not yet implemented")
    }

    override fun scrapeChapter(chapterURL: String, chapterNum: Double, chapterTitle: String) : BoxNovelChapter {
        val doc = Jsoup.connect(chapterURL).get()
        var contentHtml = doc.selectFirst("div.text-left").select("p")
        var nextChapter = ""
        var prevChapter = ""
        var nextChapterHTML = doc.selectFirst("div.nav-next a")
        nextChapterHTML?.let{
            nextChapter = it.attr("href").toString()
        }
        var prevChapterHTML = doc.selectFirst("div.nav-previous a")
        prevChapterHTML?.let{
            prevChapter = it.attr("href").toString()
        }
        val content = contentHtml.joinToString(separator = "").replace(
            "<p>", "").replace("</p>", "\n\n")

        return BoxNovelChapter(chapterNum, chapterTitle, content, chapterURL, nextChapter, prevChapter)

    }
}