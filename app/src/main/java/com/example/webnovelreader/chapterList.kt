package com.example.webnovelreader

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.util.Log.e
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import com.example.webnovelreader.websites.BoxNovel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

import kotlinx.android.synthetic.main.activity_chapter_list.*
import kotlinx.android.synthetic.main.activity_textchanging.*
import kotlinx.android.synthetic.main.content_chapter_list.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.*

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.async
import java.security.AccessController.getContext
import java.util.logging.Logger
import kotlin.Exception

class chapterList : AppCompatActivity() {
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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        hasInternetConnection().subscribe { hasInternet ->
            if (hasInternet) {
                progressbarholder.visibility = View.VISIBLE
                lifecycleScope.launch(Dispatchers.IO) {
                    val boxNovel = BoxNovel()
                    val book =
                        boxNovel.scrapeBook("https://boxnovel.com/novel/super-detective-in-the-fictional-world/")
                    runOnUiThread {
                        val appbar = findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
                        appbar.title = book.title
                        val chapterListings = findViewById<LinearLayout>(R.id.chapterListings2)
                        for (chapterNumber in book.chapterList) {
                            val chapter = TextView(this@chapterList)
                            chapter.text = chapterNumber.chapterTitle
                            chapter.textSize = 20.0F
                            chapter.setPadding(30, 20, 0, 70)
                            chapter.setOnClickListener {
                                val intent = Intent(this@chapterList, textchanging::class.java)
                                intent.putExtra("chapter_url", chapterNumber.url)
                                intent.putExtra("chapter_title", chapterNumber.chapterTitle)
                                startActivity(intent)
                            }
                            chapterListings.addView(chapter)
                        }
                    }
                }
                progressbarholder.visibility = View.GONE
            } else {
                val t = Toast.makeText(
                    this@chapterList,
                    "You are not connected to the internet",
                    Toast.LENGTH_LONG
                )
                t.show()
            }
        }
    }
}





