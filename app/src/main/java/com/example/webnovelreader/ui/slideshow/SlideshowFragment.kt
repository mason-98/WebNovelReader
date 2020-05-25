package com.example.webnovelreader.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.webnovelreader.R
import com.example.webnovelreader.ui.gallery.GalleryFragment
import com.example.webnovelreader.websites.BoxNovel
import com.example.webnovelreader.websitesimport.NovelAll

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        val source1: TextView = root.findViewById(R.id.text_slideshow)
        val source2: TextView = root.findViewById(R.id.source2)
        val navController = findNavController()
        val sourceBundle = Bundle()
        source1.setOnClickListener {
            sourceBundle.putSerializable("SourceObject", BoxNovel())
            navController.navigate(R.id.nav_gallery,sourceBundle)
        }
        source2.setOnClickListener {
            sourceBundle.putSerializable("SourceObject", NovelAll())
            navController.navigate(R.id.nav_gallery, sourceBundle)
        }
        return root
    }
}
