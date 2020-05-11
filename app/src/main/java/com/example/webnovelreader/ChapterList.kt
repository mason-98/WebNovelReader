package com.example.webnovelreader

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.webnovelreader.websites.BoxNovel
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chapter_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ChapterList : AppCompatActivity() {
    fun hasInternetConnection(): Single<Boolean> {
        return Single.fromCallable {
            try {
                // Connect to Google DNS to check for connection
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_list)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { view ->
            val cl = findViewById<LinearLayout>(R.id.chapterListings2)
            val views = ArrayList<View>()
            for(x in 0..cl.childCount-1) {
                views.add(cl.getChildAt(x))

            }
            cl.removeAllViewsInLayout()
            for(x in 0..views.size-1){
                cl.addView(views.get(x),0)
            }
        }

        progressbarholder.visibility = View.VISIBLE
        hasInternetConnection().subscribe { hasInternet ->
            if (hasInternet) {

                val boxNovel = BoxNovel()
                val book =
                    boxNovel.scrapeBook(intent.getStringExtra("bookUrl"))
                    val appbar = findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
                    appbar.title = book.title
                    val chapterListings = findViewById<LinearLayout>(R.id.chapterListings2)
                    for (chapterNumber in book.chapterList) {
                        val chapter = TextView(this@ChapterList)
                        chapter.text = chapterNumber.chapterTitle
                        chapter.textSize = 20.0F
                        chapter.setPadding(30, 20, 0, 70)
                        chapter.setOnClickListener {
                            val intent = Intent(this@ChapterList, TextChanging::class.java)
                            intent.putExtra("chapter_url", chapterNumber.url)
                            intent.putExtra("chapter_title", chapterNumber.chapterTitle)
                            startActivity(intent)
                        }
                        chapterListings.addView(chapter)
                    }
            progressbarholder.visibility = View.GONE
            } else {
                val t = Toast.makeText(
                    this@ChapterList,
                    "You are not connected to the internet",
                    Toast.LENGTH_LONG
                )
                progressbarholder.visibility = View.GONE
                t.show()
            }
        }
    }
}





