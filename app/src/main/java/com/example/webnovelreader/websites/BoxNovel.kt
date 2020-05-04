package com.example.webnovelreader.websites

import android.util.Log.d
import com.example.webnovelreader.chapters.BoxNovelChapter
import com.example.webnovelreader.interfaces.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.lang.Exception

class BoxNovel(override val baseURL: String = "https://boxnovel.com/",
               override val latestUpdateExt: String = "page/") : WebSite {

    override fun scrapeBook(bookExt: String) {
        val url = baseURL + bookExt
        val doc = Jsoup.connect(url).maxBodySize(0).get()
        val bookTitleHeader = doc.select("div.post-title h3")[0]
        var bookTitle: String
        val chapterListHTML = doc.select("li.wp-manga-chapter")
        val chapterList = chapterListHTML.map {
            val chapterInfo = it.child(0).text().toString().removePrefix(
                "Chapter "
            ).split(" - ", limit = 2)
            d("Mason", chapterInfo.toString())
            val chapterURL = it.child(0).attr("href").toString()
            try {
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
        }

        try {
            bookTitle = bookTitleHeader.childNode(2).toString().trim()
        } catch (e: Exception){
            bookTitle = bookTitleHeader.childNode(0).toString().trim()
        }
        d("Mason", bookTitle)
    }

    override fun scrapeLatestUpdates() {
        TODO("Not yet implemented")
    }

    override fun scrapeChapter(chapterURL: String, chapterNum: Double, chapterTitle: String) : BoxNovelChapter {
        val doc = Jsoup.connect(chapterURL).get()
        var contentHtml = doc.selectFirst("div.text-left").select("p")
        val content = contentHtml.joinToString(separator = "").replace(
            "<p>", "").replace("</p>", "\n\n")
        return BoxNovelChapter(chapterNum, chapterTitle, content, chapterURL)

    }
}