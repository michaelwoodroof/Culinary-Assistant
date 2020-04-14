package com.michaelwoodroof.culinaryassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_debug_menu.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class DebugMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_menu)

        // File Data

        val file = filesDir
        val files = file.listFiles()

        files?.forEach {
            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(it.name)
            val inputStreamReader = InputStreamReader(fileInputStream!!)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var line: String? = null
            while ({ line = bufferedReader.readLine(); line }() != null) {
                stringBuilder.append(line)
            }
            //Displaying data on RecyclerView
            val text = TextView(this)
            text.id = View.generateViewId()
            text.text = stringBuilder

            clFiles.addView(text)

        }

    }

    fun goBack(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        // Ensures that Data is Kept Between Screens
        val b : Button = Button(this) // Empty View
        goBack(b)
    }


}
