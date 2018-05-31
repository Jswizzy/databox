package com.wintec.blackbox.model

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs

object Coordinate {
    // Regex for parsing NMEA Coordinates (D?DD)(mm.mmmmm)
    val NMEA_COORD_REGEX = """(\d?\d?\d?)?(\d{2}\.\d+)""".toRegex()

    val df = DecimalFormat("#.####")


    /** converts NMEA format to Degrees degrees
    NMEA format is ddmm.mmmm, n/s (d)ddmm.mmmm, e/w
    To get to decimal degrees from degrees ad minutes, you use the following formula:
    (d)dd + (mm.mmmm/60) (* -1 for W and S)
     */
    fun from(string: String, isNorthOrEast: Boolean = true): Double {

        val groups = NMEA_COORD_REGEX.matchEntire(string)?.groups

        // get DDD of DDDmm.mmmm
        val degrees = groups?.get(1)?.value?.toDouble() ?: 0.0
        // get mm.mmmmm of DDDmm.mmmm
        val minutes = groups?.get(2)?.value?.toDouble() ?: 0.0

        df.roundingMode = RoundingMode.CEILING

        val degreeDegrees = (degrees + (minutes / 60)) * (if (isNorthOrEast) 1 else -1)

        return degreeDegrees
    }

    fun decimalToDMS(decimal: Double): Double {

        val degree = decimal.toInt()

        val minutes = abs(decimal - degree) * 60

        val stingValue = "$degree$minutes"

        val double = stingValue.toDouble()

        return double / 100
    }
}


fun String.toCoordinate(northOrEast: String) = Coordinate.from(this, northOrEast == "N" || northOrEast == "E")

fun Double.toDMS() = Coordinate.decimalToDMS(this)

fun main(args: Array<String>) {

    val dms = "3737.45".toCoordinate("W")

    println(dms)
}

