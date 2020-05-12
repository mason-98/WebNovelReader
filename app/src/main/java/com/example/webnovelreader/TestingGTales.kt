package com.example.webnovelreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import com.example.webnovelreader.websites.GravityTales



class TestingGTales : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_g_tales)
        val book =  GravityTales().scrapeBook("http://gravitytales.com/novel/age-of-adepts")
        d("nick", book.chapterList.toString())
    }
}
