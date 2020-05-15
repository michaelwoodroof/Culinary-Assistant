package com.michaelwoodroof.culinaryassistant.structure

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Suggestion(var tag : String, var amount : Int) : Parcelable, Serializable {}