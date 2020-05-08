package com.example.webnovelreader.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.webnovelreader.GridViewAdapter
import com.example.webnovelreader.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*
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
                galleryViewModel =
                    ViewModelProviders.of(this).get(GalleryViewModel::class.java)
                val gridView: GridView = root.findViewById(R.id.bookCoverGridView)
                galleryViewModel.bookCovers.observe(viewLifecycleOwner, Observer {
                    var adapter =
                        this.activity?.applicationContext?.let { it1 -> GridViewAdapter(it1, it) }
                    gridView.adapter = adapter
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
}
