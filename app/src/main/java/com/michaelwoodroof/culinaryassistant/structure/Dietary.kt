package com.michaelwoodroof.culinaryassistant.structure

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
class Dietary(var name: String) : Parcelable, Serializable


