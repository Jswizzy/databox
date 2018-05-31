package com.wintec.blackbox.model

import java.time.LocalTime

/**
 * GPGGA message - Global Positioning System Fix Data
 */
data class GGA(
        val time: LocalTime,
        val lat: Double?,
        val lon: Double?,
        val fix: Fix,
        val satellites: Int?,
        val hdop: Float?,
        val alt: Float?,
        val geoHeight: Float?) {
    companion object: Parser(
            """GPGGA,(\d*\.\d*),(\d*\.?\d*),([NS]?),(\d*\.?\d*),([WE]?),([0-2]?),(\d*),(\d*\.?\d*),(-?\d*\.?\d*),M,(-?\d*\.?\d*),M,.*"""
                    .toRegex()
    ) {

        fun from(string: String): GGA {

            val (time, lat, ns, lon, we, fix, numSats, hdop, alt, geoHeight) =
                    regex.matchEntire(string)?.destructured ?: throw IllegalArgumentException()

            return GGA(
                    time.toTime(),
                    lat.toCoordinate(ns),
                    lon.toCoordinate(we),
                    Fix.get(fix.toInt()),
                    numSats.toInt(),
                    hdop.toFloatOrNull(),
                    alt.toFloatOrNull(),
                    geoHeight.toFloatOrNull()
            )
        }

        fun read(text: String): List<GGA> {
            return text.splitToSequence("\n")
                    .map {
                        it.drop(1).trim()
                    }
                    .filter {
                        it.matches(regex)
                    }
                    .map {
                        from(it)
                    }
                    .toList()
        }
    }
}