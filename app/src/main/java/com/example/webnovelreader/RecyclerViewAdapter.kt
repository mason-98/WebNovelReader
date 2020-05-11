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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.webnovelreader.interfaces.BookCover
import java.net.URL


class RecyclerViewAdapter(context : Context, bookCoverList: List<BookCover>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var bookCoverList : MutableList<BookCover> = mutableListOf()
    var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var gridImage : ImageView = itemView.findViewById(R.id.grid_image)
        var gridText : TextView = itemView.findViewById(R.id.grid_text)
        var parentLayout : RelativeLayout = itemView.findViewById(R.id.bookCoverHolder)
    }

    init {
        this.bookCoverList.addAll(bookCoverList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_cover_details, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookCoverList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        val bookCover = this.bookCoverList[position]
        var bmp : Bitmap?
        bmp = try {
            GetImage().execute(bookCover.bookCoverUrl).get()
        } catch (e:Exception){
            d("Error", e.toString())
            null
        }
        holder.gridText.text = bookCover.bookTitle
        holder.gridText.width = 200
        holder.gridImage.layoutParams.height = 300
        holder.gridImage.layoutParams.width = 200
        holder.gridImage.scaleType = ImageView.ScaleType.FIT_XY
        holder.gridImage.setImageBitmap(bmp)
        holder.parentLayout.setOnClickListener {
            val intent = Intent(this.context, ChapterList::class.java)
            intent.putExtra("bookUrl", bookCover.bookUrl)
            context.startActivity(intent)
        }

    }
    private class GetImage : AsyncTask<String, Void, Bitmap>(){
        override fun doInBackground(vararg params: String): Bitmap {
            val url = URL(params[0])
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }

    }


//    fun addBooks(books: List<BookCover>) {
//        this.bookCoverList.addAll(books)
//    }

}