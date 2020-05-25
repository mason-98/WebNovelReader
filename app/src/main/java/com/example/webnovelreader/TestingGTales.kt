package com.example.webnovelreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.webnovelreader.websites.BoxNovel


class TestingGTales : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_g_tales)
        BoxNovel().scrapeBook("https://veratales.com/novel/raising-a-fox-spirit-in-my-home")
    }
}
