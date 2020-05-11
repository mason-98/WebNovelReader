package com.example.webnovelreader

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.webnovelreader.interfaces.Book
import com.example.webnovelreader.interfaces.BookCover
import kotlinx.android.synthetic.main.book_cover_details.view.*
import java.lang.Exception
import java.net.URL


class GridViewAdapter : BaseAdapter {
    private var bookCoverList : MutableList<BookCover> = mutableListOf()
    var context: Context

    constructor(context: Context, bookCoverList: List<BookCover>) : super() {
        this.bookCoverList.addAll(bookCoverList)
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val bookCover = this.bookCoverList[position]
        var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var bookCoverView = inflater.inflate(R.layout.book_cover_details, null)
        var bmp : Bitmap?
        bmp = try {
            GetImage().execute(bookCover.bookCoverUrl).get()
        } catch (e:Exception){
            d("Error", e.toString())
            null
        }
        bookCoverView.grid_image.layoutParams.height = 300
        bookCoverView.grid_image.layoutParams.width = 200
        bookCoverView.grid_image.setImageBitmap(bmp)
        bookCoverView.grid_image.scaleType = ImageView.ScaleType.FIT_XY
        bookCoverView.grid_text.text = bookCover.bookTitle
        bookCoverView.grid_text.width = 200
        bookCoverView.setOnClickListener {
            val intent = Intent(this.context, ChapterList::class.java)
            intent.putExtra("bookUrl", bookCover.bookUrl)
            context.startActivity(intent)
        }
        return bookCoverView
    }

    fun addBooks(books: List<BookCover>) {
        this.bookCoverList.addAll(books)
    }

    override fun getItem(position: Int): Any {
        return bookCoverList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return bookCoverList.size
    }

    private class GetImage : AsyncTask<String, Void, Bitmap>(){
        override fun doInBackground(vararg params: String): Bitmap {
            val url = URL(params[0])
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }

    }
}