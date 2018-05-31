package com.wintec.blackbox.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * GPRMC message - Recommended minimum specific GPS data
 * example: "GPRMC,144449.000,A,4044.7581,N,07401.5204,W,0.15,276.41,140518,,,A*71"
 */
data class RMC(
        val time: LocalTime,
        val date: LocalDate,
        val course: Float,
        val speed: Float,
        val validStatus: Status,
        val lat: Double?,
        val lon: Double?) {
    companion object : Parser(
            """GPRMC,(\d*\.?\d*),([VA]),(\d*\.?\d*),([NS]?),(\d*\.?\d*),([WE]?),(\d*\.?\d*),(\d*\.\d*),(\d{6}),.*""".toRegex()
    ) {

        fun from(message: String): RMC {

            val (time, status, lat: String, ns: String, lon: String, we: String, speed, course, date)
                    = regex.matchEntire(message)?.destructured ?: throw IllegalArgumentException()

            return RMC(
                    time = time.toTime(),
                    date = date.toDate(),
                    validStatus = Status.valueOf(status),
                    course = course.toFloat(),
                    speed = speed.toFloat(),
                    lat = lat.toCoordinate(ns),
                    lon = lon.toCoordinate(we)
            )
        }

        fun readMessages(messages: String): List<RMC> {
            return messages.splitToSequence("\n")
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