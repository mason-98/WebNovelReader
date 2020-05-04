package com.example.webnovelreader

import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import com.example.webnovelreader.websites.BoxNovel
import kotlinx.android.synthetic.main.activity_chapter_list.*
import kotlinx.android.synthetic.main.activity_textchanging.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.AccessController.getContext

class chapterList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_list)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        progressbarholder.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            val boxNovel = BoxNovel()
            val book = boxNovel.scrapeBook("https://boxnovel.com/novel/super-detective-in-the-fictional-world/")
            runOnUiThread {
                val chapterListings = findViewById<LinearLayout>(R.id.chapterListings2)
                for (chapterNumber in 1..book.chapterList.size) {
                    val chapter = TextView(this@chapterList)
                    chapter.text = "Chapter " + chapterNumber.toString()
                    chapter.textSize = 20.0F
                    chapter.setPadding(30,20,0,70)
                    chapterListings.addView(chapter)



                }
                progressbarholder.visibility = View.GONE
            }

        }

    }
}
