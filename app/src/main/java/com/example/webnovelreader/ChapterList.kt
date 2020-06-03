package com.example.webnovelreader

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.webnovelreader.database.DatabaseHelper
import com.example.webnovelreader.interfaces.WebSite
import com.example.webnovelreader.ui.home.HomeFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_chapter_list.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ChapterList : AppCompatActivity() {
    fun hasInternetConnection(): Single<Boolean> {
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_list)
        setSupportActionBar(toolbar)


        fab.setOnClickListener { _ ->
            val cl = findViewById<LinearLayout>(R.id.chapterListings2)
            val views = ArrayList<View>()
            for(x in 0..cl.childCount-1) {
                views.add(cl.getChildAt(x))

            }
            cl.removeAllViewsInLayout()
            for(x in 0..views.size-1){
                cl.addView(views.get(x),0)
            }
        }
        val dbHelper = DatabaseHelper(this.baseContext)
        val db = dbHelper.readableDatabase
        val bookUrl = intent.getStringExtra("bookUrl")
        var cursor = db.rawQuery("SELECT bookmarked FROM Book WHERE book_url = '$bookUrl'", null)
        if (cursor.count != 0) {
            cursor.moveToFirst()
            if (cursor.getInt( cursor.getColumnIndex("bookmarked")) ==  1){
                bookmarkButton.text="Unbookmark Novel"
                var imgResource = R.drawable.ic_bookmarked
                bookmarkButton.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
            }
        }
        cursor.close()
        db.close()
        dbHelper.close()
        bookmarkButton.setOnClickListener {
            val dbHelper = DatabaseHelper(this.baseContext)
            val db = dbHelper.writableDatabase
            var cv = ContentValues()
            if(bookmarkButton.text=="Bookmark Novel"){
                bookmarkButton.text="Unbookmark Novel"
                var imgResource = R.drawable.ic_bookmarked
                bookmarkButton.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
                cv.put("bookmarked", 1)
            } else {
                bookmarkButton.text="Bookmark Novel"
                var imgResource = R.drawable.ic_not_bookmarked
                bookmarkButton.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
                cv.put("bookmarked", 0)
            }
            db.update("Book", cv, "book_url = '$bookUrl'", null)
            cursor.close()
            db.close()
            dbHelper.close()
        }

        progressbarholder.visibility = View.VISIBLE
        hasInternetConnection().subscribe { hasInternet ->
            if (hasInternet) {

                //Grab the information of the book selected
                val website = intent.extras?.getSerializable("SourceObject") as WebSite
                val book =
                    website.scrapeBook(bookUrl)
                val cover = intent.extras?.getParcelable<Bitmap>("bookCover") as Bitmap

                val appbar = findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
                appbar.title = book.title

                val background = findViewById<ImageView>(R.id.coverBackground)
                background.setImageBitmap(cover)
                background.scaleType = ImageView.ScaleType.FIT_END
                //fill in author title and chapter numbers
                findViewById<TextView>(R.id.author).append(book.author)
                findViewById<TextView>(R.id.chapterNumber).append(book.chapterList.size.toString())

                //list each chapter and a corresponding onclick action
                val chapterListings = findViewById<LinearLayout>(R.id.chapterListings2)
                    for (chapterNumber in book.chapterList) {
                        val chapter = TextView(this@ChapterList)
                        chapter.text = chapterNumber.chapterTitle
                        chapter.textSize = 20.0F
                        chapter.setPadding(30, 20, 0, 70)
                        chapter.setOnClickListener {
                            val intent = Intent(this@ChapterList, ChapterContents::class.java)
                            intent.putExtra("chapter_url", chapterNumber.url)
                            intent.putExtra("chapter_title", chapterNumber.chapterTitle)
                            intent.putExtras(bundleOf(Pair("SourceObject", website)))
                            startActivity(intent)
                        }
                        chapterListings.addView(chapter)
                    }
            progressbarholder.visibility = View.GONE
            } else {
                val t = Toast.makeText(
                    this@ChapterList,
                    "You are not connected to the internet",
                    Toast.LENGTH_LONG
                )
                progressbarholder.visibility = View.GONE
                t.show()
            }
        }
    }

    override fun onBackPressed() {
        HomeFragment.adapterGrid.setData(HomeFragment.getBookmarkedBooks(this.baseContext))
        super.onBackPressed()
    }
}





