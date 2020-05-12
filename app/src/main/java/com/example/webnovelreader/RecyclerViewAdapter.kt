package com.example.webnovelreader

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.webnovelreader.interfaces.BookCover
import kotlinx.android.synthetic.main.book_cover_details.view.*
import java.net.URL


class RecyclerViewAdapter(bookCoverList: ArrayList<BookCover?>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var bookCoverList : MutableList<BookCover?> = mutableListOf()
    lateinit var context: Context

    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun getItemAtPosition(position: Int): BookCover? {
        return bookCoverList[position]
    }

    fun addLoadingView() {
        //Add loading item
        Handler().post {
            bookCoverList.add(null)
            notifyItemInserted(bookCoverList.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (bookCoverList.size != 0) {
            bookCoverList.removeAt(bookCoverList.size - 1)
            notifyItemRemoved(bookCoverList.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookCoverList[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    fun addData(dataViews: ArrayList<BookCover>) {
        this.bookCoverList.addAll(dataViews)
        notifyDataSetChanged()
    }

    init {
        this.bookCoverList.addAll(bookCoverList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.book_cover_details, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.books_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return bookCoverList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            holder.itemView.grid_text.text
            val bookCover = this.bookCoverList[position]
            var bmp: Bitmap?
            bmp = try {
                GetImage().execute(bookCover?.bookCoverUrl).get()
            } catch (e: Exception) {
                d("Error", e.toString())
                null
            }
            holder.itemView.grid_text.text = bookCover?.bookTitle
            holder.itemView.grid_text.width = 200
            holder.itemView.grid_image.layoutParams.height = 300
            holder.itemView.grid_image.layoutParams.width = 200
            holder.itemView.grid_image.scaleType = ImageView.ScaleType.FIT_XY
            holder.itemView.grid_image.setImageBitmap(bmp)
            holder.itemView.setOnClickListener {
                val intent = Intent(this.context, ChapterList::class.java)
                intent.putExtra("bookUrl", bookCover?.bookUrl)
                context.startActivity(intent)
            }
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