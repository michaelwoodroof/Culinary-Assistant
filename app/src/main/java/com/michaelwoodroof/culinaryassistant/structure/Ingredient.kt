package com.michaelwoodroof.culinaryassistant.structure

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Ingredient(var name: String, var amount: String, var unit: String,
                 var notes: String) : Parcelable, Serializable {

}