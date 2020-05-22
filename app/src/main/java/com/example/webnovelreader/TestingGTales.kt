package com.example.webnovelreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.webnovelreader.websites.BoxNovel
import com.example.webnovelreader.websitesimport.NovelAll


class TestingGTales : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_g_tales)
        val NovelAllSource = findViewById<TextView>(R.id.NovelAll)
        val BoxNovelSource = findViewById<TextView>(R.id.BoxNovelSourceText)
        val source = Bundle()
        NovelAllSource.setOnClickListener {
            source.putSerializable("Source Object", NovelAll())
        }
        BoxNovelSource.setOnClickListener {
            source.putSerializable("Source Object", BoxNovel())
        }

    }
}
