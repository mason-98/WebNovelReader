package com.example.webnovelreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log.d
import android.widget.TextView
import com.example.webnovelreader.websitesimport.NovelAll


class TestingGTales : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_g_tales)
        val x = NovelAll().scrapeBook("https://www.novelall.com/novel/World-Teacher-Other-World-Style-Education-Agent.html")
        d("nick" , x.chapterList.get(1).url)
        val contents = NovelAll().scrapeChapter("https://www.novelall.com/chapter/World-Teacher-Other-World-Style-Education-Agent-Chapter-174/1786068/","").content
        d("nick", contents)
        val lol = findViewById<TextView>(R.id.textcontents)
        lol.movementMethod = ScrollingMovementMethod()
        lol.text = contents
    }
}
