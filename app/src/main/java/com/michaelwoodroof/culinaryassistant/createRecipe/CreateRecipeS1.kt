package com.michaelwoodroof.culinaryassistant.createRecipe

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.michaelwoodroof.culinaryassistant.MainActivity
import com.michaelwoodroof.culinaryassistant.R
import com.michaelwoodroof.culinaryassistant.helper.ImageConversions
import com.michaelwoodroof.culinaryassistant.structure.Recipe
import kotlinx.android.synthetic.main.activity_create_recipe_s1.*

class CreateRecipeS1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe_s1)

        txtvTitle.setOnFocusChangeListener { _, b -> if (!b) {
                checkField(0)
            }
        }

        txtDesc.setOnFocusChangeListener { _, b -> if (!b) {
                checkField(1)
            }
        }

        val pr: Recipe? = intent.getParcelableExtra("partialrecipe")
        if (pr?.title != null && pr.title.trim() != "") {
            txtvTitle.setText(pr.title)
        }
        if (pr?.description != null && pr.description.trim() != "") {
            txtDesc.setText(pr.description)
        }
        btnNovice.tag = "nt"
        btnIntermediate.tag = "nt"
        btnAdvanced.tag = "nt"
        when (pr?.difficulty) {
            0 -> {
                btnNovice.tag = "yt"
            } 1 -> {
                btnIntermediate.tag = "yt"
            } 2 -> {
                btnAdvanced.tag = "yt"
            }
        }
        if (pr?.imgReference != null && pr?.imgReference.trim() != "") {
            imgPreview.setImageURI(pr.imgReference.toUri())
            imgPreview.tag = pr.imgReference
            imgPreview.visibility = View.VISIBLE
            btnRemove.visibility = View.VISIBLE
        }
        intent.putExtra("partialrecipe", pr as Parcelable)
        setButtons()
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

    fun goForward(view: View) {
        // Call Fields
        var boolList = ArrayList<Boolean>()
        boolList.add(checkField(0))
        boolList.add(checkField(1))
        boolList.add(checkField(2))
        boolList.add(checkField(3))
        // Check Status
        if (boolList[0] && boolList[1] && boolList[2] && boolList[3]) {
            var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
            pr?.title = txtvTitle.text.toString().trim()
            pr?.description = txtDesc.text.toString().trim()
            if (btnNovice.tag == "yt") {
                pr?.difficulty = 0
            } else if (btnIntermediate.tag == "yt") {
                pr?.difficulty = 1
            } else if (btnAdvanced.tag == "yt") {
                pr?.difficulty = 2
            } else {
                // Default Case
                pr?.difficulty = -1
            }
            if (imgPreview.tag != "" && imgPreview.tag != null) {
                pr?.imgReference = imgPreview.tag.toString()
            } else {
                pr?.imgReference = ""
            }

            val intent = Intent(this, CreateRecipeS2::class.java)
            intent.putExtra("partialrecipe", pr as Parcelable)
            startActivity(intent)
        }
    }

    fun diffSelector(view: View) {
        // Set Tags of Buttons to Toggled or Not Toggled
        txtvDiffError.visibility = View.GONE // HIDE TXTV
        txtvDiffError.setTextColor(Color.BLACK)
        txtvDifficulty.setTextColor(Color.BLACK)
        when (view.id) {
            btnNovice.id -> {
                btnNovice.tag = "yt"
                btnIntermediate.tag = "nt"
                btnAdvanced.tag = "nt"
            } btnIntermediate.id -> {
                btnNovice.tag = "nt"
                btnIntermediate.tag = "yt"
                btnAdvanced.tag = "nt"
            } btnAdvanced.id -> {
                btnNovice.tag = "nt"
                btnIntermediate.tag = "nt"
                btnAdvanced.tag = "yt"
            }
        }
        setButtons()
    }

    private fun setButtons() {
        if (btnNovice.tag == "yt") {
            btnNovice.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.csltgreen))
            btnNovice.setTextColor(ContextCompat.getColor(baseContext, R.color.csltwhite))
        } else {
            btnNovice.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.csltwhite))
            btnNovice.setTextColor(ContextCompat.getColor(baseContext, R.color.csltgreen))
        }

        if (btnIntermediate.tag == "yt") {
            btnIntermediate.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.csltorange))
            btnIntermediate.setTextColor(ContextCompat.getColor(baseContext, R.color.csltwhite))
        } else {
            btnIntermediate.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.csltwhite))
            btnIntermediate.setTextColor(ContextCompat.getColor(baseContext, R.color.csltorange))
        }

        if (btnAdvanced.tag == "yt") {
            btnAdvanced.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.csltred))
            btnAdvanced.setTextColor(ContextCompat.getColor(baseContext, R.color.csltwhite))
        } else {
            btnAdvanced.backgroundTintList =ColorStateList.valueOf(ContextCompat.getColor(baseContext, R.color.csltwhite))
            btnAdvanced.setTextColor(ContextCompat.getColor(baseContext, R.color.csltred))
        }
    }

    fun pickImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("image/*")
        // Mime Types
//        val acceptedTypes = {"image/png"}
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, acceptedTypes)
        // Launch Gallery
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data

            imgPreview.setImageURI(selectedImage)

            imgPreview.tag = selectedImage.toString()
            var pr: Recipe? = intent.getParcelableExtra("partialrecipe")
            pr!!.uriRef = selectedImage!!.toString()
            intent.putExtra("partialrecipe", pr as Parcelable)

            // Update Visibles
            txtvWarning.visibility = View.GONE
            imgPreview.visibility = View.VISIBLE
            btnRemove.visibility = View.VISIBLE
        }
    }

    fun hideImage(view: View) {
        imgPreview.visibility = View.GONE
        imgPreview.tag = ""
        btnRemove.visibility = View.GONE
    }

    // Used for Error Checking
    private fun checkField(field : Int) : Boolean {


        when (field) {

            // TITLE
            0 -> {
                val pattern = "[\\W]+".toRegex()
                return if (!(pattern.matches(txtvTitle.text.toString().trim())) && txtvTitle.text.toString().trim() != "") {
                    // Set Error
                    txtiTitle.setError("")
                    true
                } else {
                    // Reset Error
                    txtiTitle.setError("Invalid Characters")
                    false
                }
            }

            // DESCRIPTION
            1 -> {
                val pattern = "[\\W]+".toRegex()
                return if (!(pattern.matches(txtDesc.text.toString().trim())) && txtDesc.text.toString().trim() != "") {
                    // Set Error
                    txtiDesc.setError("")
                    true
                } else {
                    // Reset Error
                    txtiDesc.setError("Invalid Characters")
                    false
                }
            }

            // DIFFICULTY
            2 -> {
                return if (btnNovice.tag == "nt" && btnIntermediate.tag == "nt" && btnAdvanced.tag == "nt") {
                    txtvDiffError.visibility = View.VISIBLE
                    txtvDiffError.setTextColor(Color.RED)
                    txtvDifficulty.setTextColor(Color.RED)
                    false
                } else {
                    txtvDifficulty.setTextColor(Color.BLACK)
                    true
                }
            }

            // IMAGE
            3 -> {
                return if (imgPreview.tag == "" || imgPreview.tag == null) {
                    // Displays Caution
                    txtvWarning.visibility = View.VISIBLE
                    true
                } else {
                    true
                }
            }

        }

        return false

    }

}
