package com.wintec.blackbox.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Date {
    fun from(nmeaDate: String): LocalDate {
        val nmeaFormat = DateTimeFormatter.ofPattern("ddMMyy")
        return LocalDate.parse(nmeaDate, nmeaFormat)
    }
}

fun String.toDate() = Date.from(this)