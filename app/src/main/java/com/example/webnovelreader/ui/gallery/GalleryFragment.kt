package com.example.webnovelreader.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.webnovelreader.RecyclerViewAdapter
import com.example.webnovelreader.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        hasInternetConnection().subscribe { hasInternet ->
            if (hasInternet) {
                val recyclerView: RecyclerView = root.findViewById(R.id.bookCoverRecyclerView)
                recyclerView.layoutManager = GridLayoutManager(this.activity?.applicationContext, 3)
                galleryViewModel =
                    ViewModelProviders.of(this).get(GalleryViewModel::class.java)
                galleryViewModel.bookCovers.observe(viewLifecycleOwner, Observer {
                    var adapter =
                        this.activity?.applicationContext?.let { it1 -> RecyclerViewAdapter(it1, it) }
                    recyclerView.adapter = adapter
                })

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

    abstract class EndlessScrollListener(layoutManager: GridLayoutManager) :
        RecyclerView.OnScrollListener() {
        private var visibleThreshold = 2
        private var currentPage = 1
        private var previousTotalItemCount = 0
        private var loading = true
        private val mLayoutManager: RecyclerView.LayoutManager
        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            val totalItemCount = mLayoutManager.itemCount
            val lastVisibleItemPosition =
                (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            if (loading && totalItemCount > previousTotalItemCount) {
                loading = false
                previousTotalItemCount = totalItemCount
            }
            if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                currentPage++
                onLoadMore(currentPage, totalItemCount, view)
                loading = true
            }
        }

        abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)

        // it can LinearLayoutManager as well
        init {
            mLayoutManager = layoutManager
            visibleThreshold *= layoutManager.spanCount
        }
    }
}
