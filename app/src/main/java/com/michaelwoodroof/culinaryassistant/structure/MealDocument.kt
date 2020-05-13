package com.michaelwoodroof.culinaryassistant.structure

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class MealDocument(var day : String, var mealType : String, var time : String,
                   var uid : String, var isNoti : Boolean) : Parcelable, Serializable {

}