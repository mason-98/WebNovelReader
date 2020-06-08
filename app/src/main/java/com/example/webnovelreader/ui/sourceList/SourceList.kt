package com.example.webnovelreader.ui.sourceList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.webnovelreader.MainActivity
import com.example.webnovelreader.R
import com.example.webnovelreader.websites.BoxNovel
import com.example.webnovelreader.websitesimport.NovelAll
import kotlinx.android.synthetic.main.app_bar_main.*

class SourceList : Fragment() {

    private lateinit var sourceListViewModel: SourceListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sourcelist, container, false)
        val source1: TextView = root.findViewById(R.id.source1)
        val source2: TextView = root.findViewById(R.id.source2)
        val s1updates: TextView = root.findViewById(R.id.s1updates)
        val s2updates: TextView = root.findViewById(R.id.s2updates)
        val navController = findNavController()


        val sourceBundle = Bundle()
        sourceBundle.putSerializable("SourceObject", BoxNovel())
        sourceBundle.putBoolean("allNovels", true)

        source1.setOnClickListener {
            navController.navigate(R.id.nav_gallery,sourceBundle)
        }
        source2.setOnClickListener {
            sourceBundle.putSerializable("SourceObject", NovelAll())
            navController.navigate(R.id.nav_gallery, sourceBundle)
        }
        s1updates.setOnClickListener {
            sourceBundle.putBoolean("allNovels", false)
            navController.navigate(R.id.nav_gallery, sourceBundle)
        }
        s2updates.setOnClickListener {
            sourceBundle.putSerializable("SourceObject", NovelAll())
            sourceBundle.putBoolean("allNovels", false)
            navController.navigate(R.id.nav_gallery, sourceBundle)
        }
        return root
    }
}
