package com.michaelwoodroof.culinaryassistant.structure

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Nutrition(val name : String, val amount : String, val fat : String, val sodium : String,
val carbohydrate : String, val protein : String, val calories : String) : Parcelable, Serializable {

}