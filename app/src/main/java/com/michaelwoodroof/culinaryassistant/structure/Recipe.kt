package com.michaelwoodroof.culinaryassistant.structure

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.net.URI
import kotlin.collections.ArrayList

@Parcelize
class Recipe(var isFreezable: String, var cookTime: String, var prepTime: String,
             var difficulty: Int, var spice: Int, var id: String, var author: String,
             var courseType: String, var cuisine: String, var description: String,
             var imgReference: String, var numOfServings: String, var source: String,
             var temperature: String,
             var title: String, var dietary: ArrayList<Dietary>, var ingredients: ArrayList<Ingredient>,
             var nutrition: ArrayList<Nutrition>, var ingredientSection: ArrayList<ExtSection>,
             var stepsSection: ArrayList<ExtSection>, var keywords: ArrayList<String>,
             var steps: ArrayList<Section>, var uriRef : String) : Parcelable, Serializable {


    // Functions
    fun convertTemperature(givenUnit: String): String {

        // Temperature Codes
        // NT - No Temperature
        // CC - Celsius
        // FF - Fahrenheit
        // FO - Fan Oven (Like Celsius)
        // GM - Gas Mark
        // Format - [Unit]-[Temperature Code]
        // Brackets not in Format

        val rawTemperature = this.temperature.substring(0, this.temperature.length - 2)

        when (givenUnit) {

            "NT", "N/A" -> {
                return "NT"
            }

            "CC" -> {
                return this.temperature
            }

            "FF" -> {
                var data = rawTemperature.toDouble()
                data *= 9
                data /= 5
                data += 32
                val convert = data.toInt()
                val str = convert.toString()
                return "$str$givenUnit"
            }

            "FO" -> {
                val str = (rawTemperature.toInt() - 20).toString()
                return "$str$givenUnit"
            }

            "GM" -> {
                when (rawTemperature.toInt()) {

                    110 -> {
                        return "1/4GM"
                    }

                    120 -> {
                        return "1/2GM"
                    }

                    140 -> {
                        return "1GM"
                    }

                    150 -> {
                        return "2GM"
                    }

                    160 -> {
                        return "3GM"
                    }

                    180 -> {
                        return "4GM"
                    }

                    190 -> {
                        return "5GM"
                    }

                    200 -> {
                        return "6GM"
                    }

                    220 -> {
                        return "7GM"
                    }

                    230 -> {
                        return "8GM"
                    }

                    240 -> {
                        return "9GM"
                    }

                }
            }

            else -> return ""

        }

        return ""

    }

    fun convertToCelsius(givenUnit: String) : String {

        var suffix = "CC"
        var rawTemp = this.temperature.substring(0, this.temperature.length - 2)

        when (givenUnit) {

            "FF" -> {
                var data = rawTemp.toDouble()
                data *= 5
                data /= 9
                data -= 32
                val convert = data.toInt()
                rawTemp = convert.toString()
            }

            "FO" -> {
                rawTemp = (rawTemp.toInt() + 20).toString()
            }

            "GM" -> {
                when (rawTemp) {

                    "1/4" -> {
                        rawTemp = "110"
                    }

                    "1/2" -> {
                        rawTemp = "120"
                    }

                    "1" -> {
                        rawTemp = "140"
                    }

                    "2" -> {
                        rawTemp = "150"
                    }

                    "3" -> {
                        rawTemp = "160"
                    }

                    "4" -> {
                        rawTemp = "180"
                    }

                    "5" -> {
                        rawTemp = "190"
                    }

                    "6" -> {
                        rawTemp = "200"
                    }

                    "7" -> {
                        rawTemp = "220"
                    }

                    "8" -> {
                        rawTemp = "230"
                    }

                    "9" -> {
                        rawTemp = "240"
                    }

                }
            }

            "NT", "N/A" -> {
                suffix = ""
                rawTemp = "NT"
            }

        }

        return "$rawTemp$suffix"

    }

    // Used to Ensure Class Compatibility
    companion object {
        @JvmStatic private val serialVersionUID: Long = -5626256367765258332
    }

}
