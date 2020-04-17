package com.michaelwoodroof.culinaryassistant.structure

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Section(var stepNumber : Int, var description: String) : Parcelable, Serializable {

}