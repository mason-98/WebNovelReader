package com.example.webnovelreader.websitesimport
import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import android.util.Log.d
import com.example.webnovelreader.books.NovelAllBook
import com.example.webnovelreader.books.covers.NovelAllBookCover
import com.example.webnovelreader.chapters.NovelAllChapter
import com.example.webnovelreader.database.DatabaseHelper
import com.example.webnovelreader.interfaces.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.Serializable


class NovelAll (override val baseURL: String = "https://www.novelall.com/",
                override val latestUpdateExt: String = "list/New-Update/",
                override val directoryExt: String = "category/index_",
                override val sourceName: String = "NovelAll"
                ) : WebSite, Serializable {

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

    override fun scrapeLatestUpdates(page: String,context: Context,allNovels: Boolean) : List<BookCover> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        try{
            val url = if (allNovels) {
                baseURL + directoryExt + page
            } else {
                baseURL + latestUpdateExt
            }
            val bookCovers: List<BookCover>
            val doc = JSoupGetUrl().execute(url).get()
            val bookCoversHtml = doc.select("div.main-content li")
            bookCovers = bookCoversHtml.map {
                    val bookImg = it.selectFirst("a img").attr("src")
                    val bookTitle = it.selectFirst("a").attr("title")
                    val bookURL = it.selectFirst("a").attr("href")
                    val values = ContentValues().apply {
                    put("book_name", bookTitle)
                    put("book_url", bookURL)
                    put("book_source", "NovelAll")
                    put("book_image_source", bookImg)
                    put("bookmarked", 0)
                    // put(last_opened, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                }
                var cursor = db.rawQuery("SELECT * FROM Book WHERE book_url = '$bookURL'", null)
                if (cursor.count == 0){
                    db.insert("Book", null, values)
                }
                cursor.close()
                NovelAllBookCover(
                    bookImg,
                    bookTitle,
                    bookURL
                )

            }
            dbHelper.close()
            return bookCovers
        } catch (e:Exception){
            d("Error", e.toString())
            return emptyList()
        }
    }

    override fun scrapeChapter(chapterURL: String, chapterTitle: String) : NovelAllChapter {
        try {
            val doc = JSoupGetUrl().execute(chapterURL).get()
            val contentHtml = doc.selectFirst("section.mangaread-img div.reading-box").select("p")
            var nextChapter = ""
            var prevChapter = ""
            val nextChapterHTML = doc.select("div.mangaread-pagenav a")[0]
            nextChapterHTML?.let {
                nextChapter = it.attr("href").toString()
            }
            var prevChapterHTML = doc.select("div.mangaread-pagenav a")[1]
            prevChapterHTML?.let {
                prevChapter = it.attr("href").toString()
            }
            val content = contentHtml.joinToString(separator = "").replace("</p><p>", "\n\n").replace("<p>", "").replace("</p>", "")


            val newChapterTitle: String
            newChapterTitle = if (chapterTitle == "") {
                doc.select("section.mangaread-top div.title h1").text().toString()
            } else {
                chapterTitle
            }
            return NovelAllChapter(newChapterTitle, chapterURL, content, nextChapter, prevChapter)
        } catch (e: Exception){
            d("Error", e.toString())
            return NovelAllChapter("", "", "", "", "")
        }
    }
}