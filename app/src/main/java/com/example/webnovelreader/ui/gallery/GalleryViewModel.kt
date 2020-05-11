package com.example.webnovelreader.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webnovelreader.interfaces.BookCover
import com.example.webnovelreader.websites.BoxNovel
import java.lang.Exception

class GalleryViewModel : ViewModel() {

    private var _bookCovers = MutableLiveData<MutableList<BookCover>>().apply {
        value= arrayListOf()
        value?.addAll(BoxNovel().scrapeLatestUpdates(_page.toString()))
    }

    private var _page = 1

    fun addBookCovers(curr_page: Int){
        _page += curr_page
        _bookCovers.value?.addAll(BoxNovel().scrapeLatestUpdates(_page.toString()))
        bookCovers = _bookCovers
    }

    var bookCovers: LiveData<MutableList<BookCover>> = _bookCovers
}