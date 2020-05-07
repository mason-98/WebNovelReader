package com.example.webnovelreader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.webnovelreader.interfaces.BookCover
import kotlinx.android.synthetic.main.book_cover_details.view.*
import org.jsoup.Jsoup
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class GridViewAdapter : BaseAdapter {
    private var bookCoverList : List<BookCover>
    var context: Context? = null

    constructor(context: Context, bookCoverList: List<BookCover>) : super() {
        this.bookCoverList = bookCoverList
        this.context = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val bookCover = this.bookCoverList[position]
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var bookCoverView = inflator.inflate(R.layout.book_cover_details, null)
        var bmp : Bitmap?
        bmp = try {
            GetImage().execute(bookCover.bookCoverUrl).get()
        } catch (e:Exception){
            d("Error", e.toString())
            null
        }
        bookCoverView.grid_image.setImageBitmap(bmp)
        bookCoverView.grid_text.text = bookCover.bookTitle

        return bookCoverView
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
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            return bmp
        }

    }
}