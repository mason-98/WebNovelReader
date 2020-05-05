package com.example.webnovelreader

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.text.Layout
import android.util.Log.d
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.example.webnovelreader.websites.BoxNovel
import kotlinx.android.synthetic.main.activity_textchanging.*
import kotlinx.android.synthetic.main.novel_apperance_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import kotlin.math.abs


class textchanging : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textchanging)
        setSupportActionBar(findViewById(R.id.readertoolbar))
        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPref.getInt("text_size", 14)
        val chapterUrl = intent.getStringExtra("chapter_url")
        val chapterTitle = intent.getStringExtra("chapter_title")
        anytext.setTextColor(Color.parseColor(sharedPref!!.getString("text_colour", "#FFFFFF")))
        background.setBackgroundColor(Color.parseColor(sharedPref!!.getString("background_colour","#000000")))
        readersettings.setOnClickListener {
            val dialog = ReaderOptions()
            dialog.show(supportFragmentManager, "ReaderOptions")

        }
        this@textchanging.supportActionBar?.title = chapterTitle




        lifecycleScope.launch(Dispatchers.IO) {
            val boxNovel = BoxNovel()
            val chapter = boxNovel.scrapeChapter(chapterUrl,chapterTitle)
            runOnUiThread {

                anytext.text = chapter.content
            }
        }
        anytext.setOnTouchListener(object : OnSwipeTouchListener(this){
            override fun onSwipeRight() {
                super.onSwipeRight()
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
            }
        })
    }

    open class OnSwipeTouchListener(ctx: Context?) : View.OnTouchListener {
        private val gestureDetector: GestureDetector = GestureDetector(ctx, GestureListener())

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                var result = false
                try {
                    val diffY: Float = e2.y - e1.y
                    val diffX: Float = e2.x - e1.x
                    if (abs(diffX) > abs(diffY)) {
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return result
            }
        }

        open fun onSwipeRight() {}
        open fun onSwipeLeft() {}
        open fun onSwipeTop() {}
        open fun onSwipeBottom() {}
    }

    class ReaderOptions : SuperBottomSheetFragment(), AdapterView.OnItemSelectedListener {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            val dialog = inflater.inflate(R.layout.novel_apperance_settings,container,false)
            // The code to create the text theme dropdown
            val spinner: Spinner? = dialog.findViewById(R.id.textspinner)
            context?.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.textcolour,
                    android.R.layout.simple_spinner_item
                ).also {adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner?.adapter = adapter
                }
            }
            spinner?.onItemSelectedListener = this

            // define the seekbar to select the text size
            val textsizeselect: SeekBar = dialog.findViewById(R.id.textsizeselector)
            // Grab default settings for displaying the example text to the user
            val sharedPref = this.activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            textsizeselect.progress = sharedPref!!.getInt("text_size", 14)
            val exampletext: TextView = dialog.findViewById(R.id.exampleText)
            exampletext.textSize = textsizeselect.progress.toFloat()
            //not sure how this works but it does
            val textbox = this.activity?.findViewById<TextView>(R.id.anytext)
            //define the actions of the seeekbar
            textsizeselect?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    exampleText.textSize = textsizeselect.progress.toFloat()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    textsizeselect.progress = sharedPref!!.getInt("text_size",14)
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    textbox?.textSize = textsizeselect.progress.toFloat()
                    val edit = sharedPref?.edit()
                    edit?.putInt("text_size",textsizeselect.progress)
                    edit?.commit()
                }

            })
            return dialog
        }


        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            val colour = p0?.getItemAtPosition(p2)
            val textbox = this.activity?.findViewById<TextView>(R.id.anytext)
            val background = this.activity?.findViewById<ConstraintLayout>(R.id.background)
            val sharedPref = this.activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val edit = sharedPref?.edit()
            ( p0?.getChildAt(0) as TextView).setTextColor(Color.BLACK)
            if (colour == "Black") {
                textbox?.setTextColor(Color.parseColor("#000000"))
                background?.setBackgroundColor(Color.parseColor("#FFFFFF"))
                edit?.putString("text_colour", "#000000")
                edit?.putString("background_colour", "#FFFFFF")
                edit?.commit()

            } else if (colour == "Yellow") {
                textbox?.setTextColor(Color.parseColor("#5F4B32"))
                background?.setBackgroundColor(Color.parseColor("#FBF0D9"))
                edit?.putString("text_colour", "#5F4B32")
                edit?.putString("background_colour", "#FBF0D9")
                edit?.commit()


            } else if (colour == "White") {
                textbox?.setTextColor(Color.parseColor("#FFFFFF"))
                background?.setBackgroundColor(Color.parseColor("#000000"))
                edit?.putString("text_colour", "#FFFFFF")
                edit?.putString("background_colour", "#000000")
                edit?.commit()
            } else if (colour == "Default") {
                textbox?.setTextColor(
                    Color.parseColor(
                        sharedPref!!.getString(
                            "text_colour",
                            "#FFFFFF"
                        )
                    )
                )
                background?.setBackgroundColor(
                    Color.parseColor(
                        sharedPref!!.getString(
                            "background_colour",
                            "#000000"
                        )
                    )
                )
            }
            d("nick", "selected  colour is $colour")
        }
    }

}


