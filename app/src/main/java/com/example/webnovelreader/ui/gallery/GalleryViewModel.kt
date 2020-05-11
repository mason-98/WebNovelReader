package com.example.webnovelreader.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webnovelreader.interfaces.BookCover
import com.example.webnovelreader.websites.BoxNovel
import java.lang.Exception

class GalleryViewModel : ViewModel() {

    private val _bookCovers = MutableLiveData<List<BookCover>>().apply {
        value = BoxNovel().scrapeLatestUpdates(_page.toString())
    }

    private var _page = 1

    fun incPage(){
        _page += 1
        _bookCovers.value = BoxNovel().scrapeLatestUpdates(_page.toString())
        bookCovers = _bookCovers
    }

    var bookCovers: LiveData<List<BookCover>> = _bookCovers
}