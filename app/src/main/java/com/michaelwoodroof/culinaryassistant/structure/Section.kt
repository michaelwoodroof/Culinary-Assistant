package com.michaelwoodroof.culinaryassistant.structure

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Section(var stepNumber : Int, var description: String) : Parcelable {

}