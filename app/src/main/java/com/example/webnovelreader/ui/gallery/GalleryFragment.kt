package com.example.webnovelreader.ui.gallery

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webnovelreader.R
import com.example.webnovelreader.RecyclerViewAdapter
import com.example.webnovelreader.RecyclerViewLoadMoreScroll
import com.example.webnovelreader.interfaces.BookCover
import com.example.webnovelreader.interfaces.OnLoadMoreListener
import com.example.webnovelreader.websites.BoxNovel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gallery.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class GalleryFragment : Fragment() {
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var bookCovers: ArrayList<BookCover?>
    private lateinit var loadMoreItemsCells: ArrayList<Int?>
    private lateinit var adapterGrid: RecyclerViewAdapter
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var curr_page = 2
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

                //** Set the scrollListerner of the RecyclerView
                setRVScrollListener()

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
            bookCovers = ArrayList(BoxNovel().scrapeLatestUpdates((1).toString(),it))
        }
    }

    private fun setAdapter() {
        adapterGrid = RecyclerViewAdapter(bookCovers)
        adapterGrid.notifyDataSetChanged()
        bookCoverRecyclerView.adapter = adapterGrid
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

    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })

        bookCoverRecyclerView.addOnScrollListener(scrollListener)
    }

    private fun LoadMoreData() {
        //Add the Loading View
        adapterGrid.addLoadingView()

        //Get the number of the current Items of the main Arraylist
        val start = adapterGrid.itemCount
        //Load 16 more items
        val end = start + 16
        //Use Handler if the items are loading too fast.
        //If you remove it, the data will load so fast that you can't even see the LoadingView
        Handler().postDelayed({
            //Create the loadMoreItemsCells Arraylist
            //Remove the Loading View
            adapterGrid.removeLoadingView()

            this.context?.let{
                var loadMoreBookCovers = ArrayList(BoxNovel().scrapeLatestUpdates((curr_page++).toString(),it))
                //We adding the data to our main ArrayList
                adapterGrid.addData(loadMoreBookCovers)
            }
            //Change the boolean isLoading to false
            scrollListener.setLoaded()
            //Update the recyclerView in the main thread
            bookCoverRecyclerView.post {
                adapterGrid.notifyDataSetChanged()
            }
        }, 3000)

    }

}
