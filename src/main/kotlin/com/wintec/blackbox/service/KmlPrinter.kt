package com.wintec.blackbox.service


import com.wintec.blackbox.model.Track
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode
import de.micromata.opengis.kml.v_2_2_0.Kml
import java.io.StringWriter

/**
 * KML IO class
 */
object KmlPrinter {
    /**
     * Convert track list to KML class for creation of a KML document
     */
    fun toKml(track: Track, description: String = "GPS data", name: String = "GPS Data"): Kml {

        val coords = track?.asSequence()
                ?.map { it.coord.toString() }
                ?.toList()

        val whens = track?.asSequence()
                ?.map { it.atTime.toString() }
                ?.toList()

        val kml = Kml()

        kml.createAndSetPlacemark()
                .withDescription(description)
                .withName(name).withOpen(true)
                .createAndSetTrack()
                .withAltitudeMode(AltitudeMode.ABSOLUTE)
                .withCoord(coords)
                .withWhen(whens)

        return kml
    }

    /**
     * create KML and output to a String
     */
    fun toText(kml: Kml): String {
        val writer = StringWriter()

        kml.marshal(writer)

        return writer.toString()
    }
}