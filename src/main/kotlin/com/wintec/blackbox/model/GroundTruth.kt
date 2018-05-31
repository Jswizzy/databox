package com.wintec.blackbox.model

import java.time.LocalDate
import java.time.LocalTime

/**
The ground truth is acquired using GPS recorders; the data is transferred using the CSV forma
Example Header: TestSite_2017Apr10_Run1_UAS1_ GroundTruth_GPS1
 */
data class GroundTruth(
        val localDate: LocalDate? = null,
        val localTime: LocalTime? = null, //UTC date and time in the format yyyy-mm-dd HH:MM:SS:fff
        val fixValid: Fix? = null, //Gps data is valid in 0 or 1
        val lat: Double? = null, //WGS-84 latitude in decimal degrees
        val lon: Double? = null, //WGS-84 longitude in decimal degrees
        val gpsAlt: Float? = null, //Altitude above mean sea level inn meters
        val barometerAlt: Double? = null, //Barometric Altitude
        val hdop: Float? = null, //Horizontal Dilution of Precision
        val nSat: Int? = null,
        val sog: Float? = null, //Speed over ground estimate of the target in meters/second
        val cog: Float? = null, //True course over ground of the target movement degrees easterly from True North in degrees
        val pressure: Double? = null, //barometer pressure
        val temperature: Double? = null, //barometer temperature
        val userLoggerID: Int? = null,
        val rfRssi: Int? = null) {

    override fun toString(): String {
        return "$localDate $localTime,$fixValid,$lat,$lon,$gpsAlt,$hdop,$cog,$sog,$pressure,$temperature,$barometerAlt,$userLoggerID"
    }

    companion object {
        const val header = "Measurement_DateTime,GPS_FixValid,GPS_lat,GPS_lon,GPS_alt,GPS_HDOP,GPS_COG,GPS_SOG,Barometer_Pressure,Barometer_Temperature,Barometer_Altitude,User_LogID"

        fun print(list: List<GroundTruth>)= "$header\n${list.joinToString("\n")}"

        fun read(text: String, id: Int = -1) = text.splitToSequence("\n")
                .map {
                    val trim = it.drop(1).trim()

                    when {
                        GGA.regex.matches(trim) -> GGA.from(trim)
                        RMC.regex.matches(trim) -> RMC.from(trim)
                        BaroAlt.regex.matches(trim) -> BaroAlt.from(trim)
                        else -> null
                    }
                }
                .filterNotNull()
                .chunked(3)
                .map {

                    var gga: GGA? = null
                    var baroAlt: BaroAlt? = null
                    var rmc: RMC? = null

                    it.forEach {
                        when (it) {
                            is GGA -> gga = it
                            is RMC -> rmc = it
                            is BaroAlt -> baroAlt = it
                        }
                    }

                    GroundTruth(
                            localDate = rmc?.date,
                            localTime = gga?.time,
                            fixValid = gga?.fix,
                            lat = gga?.lat ?: rmc?.lat,
                            lon = gga?.lon ?: rmc?.lon,
                            gpsAlt = gga?.alt,
                            hdop = gga?.hdop,
                            barometerAlt = baroAlt?.alt,
                            pressure = baroAlt?.pressure,
                            temperature = baroAlt?.temp,
                            cog = rmc?.course,
                            sog = rmc?.speed,
                            userLoggerID = id
                    )
                }
                .toList()
    }
}
