package com.michaelwoodroof.culinaryassistant.helper

object CategoryColor {

    fun getColor(category : String) : String {

        when (category) {

            "American"      -> return "#5E96AE"
            "British"       -> return "#89AEB2"
            "Caribbean"     -> return "#FDCF76"
            "Chinese"       -> return "#E58B88"
            "French"        -> return "#9DABDD"
            "Greek"         -> return "#8AC0DE"
            "Indian"        -> return "#D7E7A9"
            "Italian"       -> return "#B5BA7E"
            "Japanese"      -> return "#A02C2D"
            "Korean"        -> return "#909090"
            "Mediterranean" -> return "#E18D96"
            "Mexican"       -> return "#BC85A3"
            "Moroccan"      -> return "#9E6B55"
            "Other"         -> return "#619196"
            "Spanish"       -> return "#DFC7C1"
            "Turkish"       -> return "#AB6393"
            "Vietnamese"    -> return "#2E8364"

        }

        return "#000000"
    }

}