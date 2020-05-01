package com.example.webnovelreader

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Layout
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import kotlinx.android.synthetic.main.activity_textchanging.*
import org.w3c.dom.Text


class textchanging : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textchanging)
        setSupportActionBar(findViewById(R.id.readertoolbar))



        readersettings.setOnClickListener {
            val dialog = ReaderOptions()
            dialog.show(supportFragmentManager, "ReaderOptions")

        }
    }


    class ReaderOptions : SuperBottomSheetFragment(), AdapterView.OnItemSelectedListener {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            val dialog = inflater.inflate(R.layout.novel_apperance_settings,container,false)
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
            return dialog
        }


        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            val colour = p0?.getItemAtPosition(p2)
            val textbox = this.activity?.findViewById<TextView>(R.id.anytext)
            val background = this.activity?.findViewById<ConstraintLayout>(R.id.background)
            ( p0?.getChildAt(0) as TextView).setTextColor(Color.BLACK)
            if (colour == "Black") {
                textbox?.setTextColor(Color.parseColor("#000000"))
                background?.setBackgroundColor(Color.parseColor("#FFFFFF"))

            } else if (colour == "Yellow") {
                textbox?.setTextColor(Color.parseColor("#5F4B32"))
                background?.setBackgroundColor(Color.parseColor("#FBF0D9"))


            } else if (colour == "White") {
                textbox?.setTextColor(Color.parseColor("#FFFFFF"))
                background?.setBackgroundColor(Color.parseColor("#000000"))
            }
            d("nick", "selected  colour is $colour")
        }
    }

}


