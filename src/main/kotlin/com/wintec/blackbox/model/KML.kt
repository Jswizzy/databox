package com.wintec.blackbox.model

import java.time.LocalDateTime

typealias Track = List<KML>?

data class KML(val coord: Coord, val atTime: AtTime) {
    companion object {
        fun fromGroundTruth(groundTruth: GroundTruth?):KML? {
            return if (groundTruth?.lat != null && groundTruth.lon != null && groundTruth.localDate != null && groundTruth.localTime != null) {
                KML(
                        Coord(
                                groundTruth.lat,
                                groundTruth.lon,
                                groundTruth.barometerAlt
                        ),
                        AtTime(
                                LocalDateTime.of(
                                        groundTruth.localDate,
                                        groundTruth.localTime
                                )
                        )
                )
            } else null
        }
    }
}

data class Coord(val lat: Double, val lon: Double, val alt: Double?) {
    override fun toString(): String {
        return "$lon $lat $alt"
    }
}

data class AtTime(val time: LocalDateTime) {
    override fun toString(): String {
        return "${time.toLocalDate()}T${time.toLocalTime()}Z"
    }
}
