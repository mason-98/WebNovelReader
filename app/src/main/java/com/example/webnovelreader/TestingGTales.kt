package com.example.webnovelreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.webnovelreader.websitesimport.VeraTales


class TestingGTales : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_g_tales)
        VeraTales().scrapeBook("https://veratales.com/novel/raising-a-fox-spirit-in-my-home")
    }
}
