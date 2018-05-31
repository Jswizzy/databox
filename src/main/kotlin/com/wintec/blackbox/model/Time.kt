package com.wintec.blackbox.model

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Time {
    fun from(nmeaTime: String): LocalTime {
        val nmeaFormat = DateTimeFormatter.ofPattern("HHmmss")
        return LocalTime.parse(nmeaTime, nmeaFormat)
    }
}

fun String.toTime() = Time.from(this.take(6))