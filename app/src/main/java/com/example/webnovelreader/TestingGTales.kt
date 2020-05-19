package com.example.webnovelreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.example.webnovelreader.websitesimport.NovelAll


class TestingGTales : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_g_tales)
        val x = NovelAll().scrapeBook("https://www.novelall.com/novel/World-Teacher-Other-World-Style-Education-Agent.html")
        d("nick" , x.chapterList.get(1).url)
    }
}
