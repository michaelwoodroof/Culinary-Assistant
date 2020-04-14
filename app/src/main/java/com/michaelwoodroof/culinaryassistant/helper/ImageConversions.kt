package com.michaelwoodroof.culinaryassistant.helper

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream

object ImageConversions {

    private var contentResolver: ContentResolver? = null

    fun uriToBitMap(u : Uri, c : ContentResolver) : Bitmap {

        val bitMap : Bitmap

        bitMap = MediaStore.Images.Media.getBitmap(c, Uri.parse(u.toString()))

        return bitMap

    }

    fun bitMapToString(bMap : Bitmap): String {

        val byteArr = ByteArrayOutputStream()
        bMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArr)
        val b : ByteArray = byteArr.toByteArray()

        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun stringToBitMap(bMapStr : String) : Bitmap {

        val imageBytes = Base64.decode(bMapStr, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    }

    // Example Call
    //imgPreview.setImageBitmap(stringToBitMap(imgPreview.tag.toString()))

}