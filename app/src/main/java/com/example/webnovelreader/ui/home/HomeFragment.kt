package com.example.webnovelreader.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webnovelreader.R
import com.example.webnovelreader.RecyclerViewAdapter
import com.example.webnovelreader.RecyclerViewLoadMoreScroll
import com.example.webnovelreader.TestingGTales
import com.example.webnovelreader.books.covers.BoxNovelBookCover
import com.example.webnovelreader.books.covers.NovelAllBookCover
import com.example.webnovelreader.database.DatabaseHelper
import com.example.webnovelreader.interfaces.Book
import com.example.webnovelreader.interfaces.BookCover
import com.example.webnovelreader.interfaces.OnLoadMoreListener
import com.example.webnovelreader.interfaces.WebSite
import com.example.webnovelreader.websitesimport.NovelAll
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var bookCovers: ArrayList<BookCover?>
    private lateinit var adapterGrid: RecyclerViewAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class GridSpacingItemDecoration(spanCount:Int, spacing:Int) : RecyclerView.ItemDecoration() {
        private var spanCount = spanCount
        private var spacing = spacing


        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column+1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing

        }
    }
    private fun hasInternetConnection(): Single<Boolean> {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        hasInternetConnection().subscribe { hasInternet ->
            if (hasInternet) {
                setItemsData()
                //** Set the adapterLinear of the RecyclerView
                setAdapter()

                //** Set the Layout Manager of the RecyclerView
                setRVLayoutManager()


            } else {
                val t = Toast.makeText(
                    this.activity?.applicationContext,
                    "You are not connected to the internet",
                    Toast.LENGTH_LONG
                )
                t.show()
            }
        }
        return root
    }

    private fun setItemsData() {

        this.context?.let{
            bookCovers = getBookmarkedBooks(it)
        }

    }

    private fun setAdapter() {
        adapterGrid = RecyclerViewAdapter(bookCovers)
        adapterGrid.notifyDataSetChanged()
    }

    private fun setRVLayoutManager() {
        mLayoutManager = GridLayoutManager(this.activity?.applicationContext, 3)
        bookCoverRecyclerView.layoutManager = mLayoutManager
        bookCoverRecyclerView.setHasFixedSize(true)
        val gridSpace : GridSpacingItemDecoration = GridSpacingItemDecoration(3,30)
        bookCoverRecyclerView.addItemDecoration(gridSpace)
        bookCoverRecyclerView.adapter = adapterGrid
        (mLayoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapterGrid.getItemViewType(position)) {
                    VIEW_TYPE_ITEM -> 1
                    VIEW_TYPE_LOADING -> 3 //number of columns of the grid
                    else -> -1
                }
            }
        }
    }

    private fun getBookmarkedBooks(context: Context): ArrayList<BookCover?> {
            val dbHelper = DatabaseHelper(context)
            val db = dbHelper?.readableDatabase
            var cursor = db.rawQuery("SELECT * FROM Book WHERE bookmarked = 1 ORDER BY book_name ASC", null)
            var bookcovers = ArrayList<BookCover?>()
            while(cursor.moveToNext()){
                var book_cover : BookCover
                if(cursor.getString(cursor.getColumnIndex("book_source"))=="BoxNovel"){
                    book_cover = BoxNovelBookCover(
                        cursor.getString(cursor.getColumnIndex("book_image_source")),
                        cursor.getString(cursor.getColumnIndex("book_name")),
                        cursor.getString(cursor.getColumnIndex("book_url")))
                } else {
                    book_cover = NovelAllBookCover(
                        cursor.getString(cursor.getColumnIndex("book_image_source")),
                        cursor.getString(cursor.getColumnIndex("book_name")),
                        cursor.getString(cursor.getColumnIndex("book_url")))
                }
                bookcovers.add(book_cover)
            }
            return bookcovers
    }


}
