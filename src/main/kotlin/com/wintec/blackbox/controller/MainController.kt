package com.wintec.blackbox.controller


import com.wintec.blackbox.model.*
import com.wintec.blackbox.service.KmlPrinter
import com.wintec.inu.service.IO
import de.micromata.opengis.kml.v_2_2_0.Kml
import javafx.collections.ObservableList
import tornadofx.*
import java.io.File

class MainController : Controller() {

    var gga: ObservableList<GGA> = listOf<GGA>().observable()
    var groundTruth: ObservableList<GroundTruth> = listOf<GroundTruth>().observable()
    var baroAlt: ObservableList<BaroAlt> = listOf<BaroAlt>().observable()
    var rmc: ObservableList<RMC> = listOf<RMC>().observable()
    var text: String = ""
    var kmlText: String = ""
    var kml: Kml? = null
    var id: Int = -1



    var file: File? = null

    private val io: IO by inject()

    /**
     * convert file to Ground Truth and save it <path>-copy.txt
     */
    fun saveToGroundTruth() {

        val out = GroundTruth.print(groundTruth)

        if (file != null) {
            io.textToFile(file, out)
        }
    }

    /**
     * convert file to KML and save it <path>.kml
     */
    fun saveToKml() {
        if (file != null) {
            val kmlFileName = nameFile(file)
            val kmlFile = File(kmlFileName)
            kml?.marshal(kmlFile)
        }
    }


    fun convertDirectoryToGroundTruth() {
        io.convertDirectory(::fileToGroundTruthString)
    }

    fun convertDirectoryToKML() {
        io.saveDirectory(::saveFileToKml)
    }

    /**
     * opens file chooser and sets open file
     */
    fun openFile() {
        file = io.openFile()
    }

    /**
     * Opens file chooser and reads a file converting and parsing it into baroAlt, GGA, RMC, Ground Truth, and KML
     */
    fun readFile() {
        openFile()
        text = file?.readText() ?: ""

        id = LoggerId.fromText(text)
        baroAlt()
        gga()
        rmc()
        groundTruth()
        kml()
    }

    /**
     * parses baroAlt data from open file
     */
    fun baroAlt() {
        baroAlt = BaroAlt.read(text).observable()
    }

    /**
     * parses gga data from open file
     */
    fun gga() {
        gga = GGA.read(text).observable()
    }

    /**
     * parses rmc data from open file
     */
    fun rmc() {
        rmc = RMC.readMessages(text).observable()
    }

    /**
     * parses groundTruth data from open file
     */
    fun groundTruth() {
        groundTruth = GroundTruth.read(text, id).observable()
    }

    /**
     * parses kml data from open file
     */
    fun kml() {

        val list = groundTruth.asSequence()
                .map {
                    KML.fromGroundTruth(it)
                }
                .filterNotNull()
                .toList()

        val name = if (id != -1) id else ""

        kml = KmlPrinter.toKml(list, name = "GPS $name Data")

        kmlText = if (kml != null) KmlPrinter.toText(kml as Kml) else ""
    }

    /**
     * helper class to rename file
     */
    fun nameFile(file: File?) = "${file?.path?.dropLast(4)}.kml"

    private fun saveFileToKml(file: File) {
        val readText = file.readText()
        val id = LoggerId.fromText(readText)
        val groundTruth = GroundTruth.read(readText, id)
        val tracks = groundTruth.asSequence()
                .map {
                    KML.fromGroundTruth(it)
                }
                .filterNotNull()
                .toList()
        val kml1 = KmlPrinter.toKml(tracks)
        val name = nameFile(file)
        kml1.marshal(File(name))
    }

    private fun fileToGroundTruthString(file: File): String {
        val readText = file.readText()
        val id = LoggerId.fromText(readText)
        val groundTruth = GroundTruth.read(readText, id)
        return GroundTruth.print(groundTruth)
    }


}