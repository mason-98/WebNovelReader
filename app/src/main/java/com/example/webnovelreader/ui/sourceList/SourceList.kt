package com.example.webnovelreader.ui.sourceList

import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.webnovelreader.MainActivity
import com.example.webnovelreader.R
import com.example.webnovelreader.interfaces.WebSite
import com.example.webnovelreader.websites.BoxNovel
import com.example.webnovelreader.websitesimport.NovelAll
import kotlinx.android.synthetic.main.app_bar_main.*
import org.w3c.dom.Text
import org.xmlpull.v1.XmlPullParser

class SourceList : Fragment() {

    private lateinit var sourceListViewModel: SourceListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sourcelist, container, false)

        val navController = findNavController()
        val sourcelist: LinearLayout = root.findViewById(R.id.sourcelist)


        val websiteMap = hashMapOf<String, WebSite>()
        websiteMap["BoxNovel"] = BoxNovel()
        websiteMap["NovelAll"] = NovelAll()


        val sourceBundle = Bundle()

        val paddingDp = resources.getDimensionPixelSize((R.dimen.sourcepadding))

        for ((key, value) in websiteMap) {
            val sourcelayout: LinearLayout = LinearLayout(this.context)
            sourcelayout.orientation = LinearLayout.HORIZONTAL
            sourcelayout.setPadding(paddingDp,paddingDp,paddingDp,paddingDp)

            val source: TextView = TextView(this.context)
            source.text = key
            source.layoutParams = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT,1F)
            source.textSize = 20F
            source.gravity = Gravity.START
            source.setOnClickListener {
                sourceBundle.putSerializable("SourceObject", value)
                sourceBundle.putBoolean("allNovels", true)
                navController.navigate(R.id.nav_gallery,sourceBundle)
            }

            val sourcelatest: TextView = TextView(this.context)
            sourcelatest.text = "Latest"
            sourcelatest.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT
                ,LinearLayout.LayoutParams.MATCH_PARENT)
            sourcelatest.gravity = Gravity.BOTTOM
            sourcelatest.textSize = 17F
            sourcelatest.setTextColor(ResourcesCompat.getColor(resources,R.color.design_default_color_secondary_variant,null))
            sourcelatest.setOnClickListener {
                sourceBundle.putSerializable("SourceObject", value)
                sourceBundle.putBoolean("allNovels", false)
                navController.navigate(R.id.nav_gallery,sourceBundle)
            }
            sourcelayout.addView(source)
            sourcelayout.addView(sourcelatest)
            sourcelist.addView(sourcelayout)
        }



        return root
    }
}
