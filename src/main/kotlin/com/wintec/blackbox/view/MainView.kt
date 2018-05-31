package com.wintec.blackbox.view

import com.wintec.blackbox.app.Styles
import com.wintec.blackbox.controller.MainController
import com.wintec.blackbox.model.BaroAlt
import com.wintec.blackbox.model.GGA
import com.wintec.blackbox.model.GroundTruth
import com.wintec.blackbox.model.RMC
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.layout.VBox
import tornadofx.*


class MainView : View("Black Box GPS Tool") {

    private val controller: MainController by inject()

    var preview: TextArea by singleAssign()
    var kml: TextArea by singleAssign()
    var baroAlt: TableView<BaroAlt> by singleAssign()
    var gga: TableView<GGA> by singleAssign()
    var rma: TableView<RMC> by singleAssign()
    var groundTruth: TableView<GroundTruth> by singleAssign()
    var combo: ComboBox<String> by singleAssign()

    val items = listOf("Ground Truth", "KML")
    val selectedformat = SimpleStringProperty(items.first())

    override val root = vbox {
        addClass(Styles.appSizeRule)

        menubar {
            menu("File") {
                item("Open File") {
                    setOnAction {
                        openFile()
                    }
                }
                separator()
                item("Convert File") {
                    setOnAction {
                        convert()
                    }
                }

                item("Convert Directory") {
                    setOnAction {
                        convertDirectory()
                    }
                }
                separator()
                item("Exit") {
                    setOnAction {
                        Platform.exit()
                    }
                }
            }
        }
        hbox(8) {
            label("Convert to:")
            combo = combobox(selectedformat, FXCollections.observableArrayList(items))

            button("Convert") {
                action {
                    convert()
                }

            }
        }

        tabpane {
            prefWidthProperty().bind(this@vbox.widthProperty())
            prefHeightProperty().bind(this@vbox.heightProperty())

            tab("NMEA", VBox()) {
                preview = textarea {

                    isEditable = false
                }
            }
            tab("BAROALT", VBox()) {
                baroAlt = tableview {

                    column("Pressure", BaroAlt::pressure).weightedWidth(1.0)
                    column("Temperature", BaroAlt::temp).weightedWidth(1.0)
                    column("Altitude", BaroAlt::alt).weightedWidth(1.0)

                }
            }
            tab("GGA", VBox()) {
                gga = tableview {

                    column("Time", GGA::time).weightedWidth(1.0)
                    column("Latitude", GGA::lat).weightedWidth(1.0)
                    column("Longitude", GGA::lon).weightedWidth(1.0)
                    column("Altitude", GGA::alt).weightedWidth(1.0)
                    column("Fix", GGA::fix).weightedWidth(1.0)
                    column("Num satellites", GGA::satellites).weightedWidth(1.0)
                    column("HDOP", GGA::hdop).weightedWidth(1.0)
                    column("GHD", GGA::geoHeight).weightedWidth(1.0)

                }
            }
            tab("RMC", VBox()) {
                rma = tableview {

                    column("Date", RMC::date).weightedWidth(1.0)
                    column("Time", RMC::time).weightedWidth(1.0)
                    column("Status", RMC::validStatus).weightedWidth(1.0)
                    column("Latitude", RMC::lat).weightedWidth(1.0)
                    column("Longitude", RMC::lon).weightedWidth(1.0)
                    column("Speed", RMC::speed).weightedWidth(1.0)
                    column("Course", RMC::course).weightedWidth(1.0)

                }
            }
            tab("Ground Truth", VBox()) {
                groundTruth = tableview {
                    column("Measure Date", GroundTruth::localDate)
                    column("Time", GroundTruth::localTime).weightedWidth(1.0)
                    column("FixValid", GroundTruth::fixValid).weightedWidth(1.0)
                    column("GPS lat", GroundTruth::lat).weightedWidth(1.0)
                    column("GPS lon", GroundTruth::lon).weightedWidth(1.0)
                    column("GPS alt", GroundTruth::gpsAlt).weightedWidth(1.0)
                    column("GPS HDOP", GroundTruth::hdop).weightedWidth(1.0)
                    column("GPS COG", GroundTruth::cog).weightedWidth(1.0)
                    column("GPS SOG", GroundTruth::sog).weightedWidth(1.0)
                    column("Baro Press", GroundTruth::pressure).weightedWidth(1.0)
                    column("Baro Tempe", GroundTruth::temperature).weightedWidth(1.0)
                    column("Baro Alt", GroundTruth::barometerAlt).weightedWidth(1.0)
                    column("LoggerId", GroundTruth::userLoggerID).weightedWidth(1.0)

                }
            }
            tab("KML", VBox()) {
                kml = textarea {

                    isEditable = false
                }
            }
        }
    }

    private fun convert() {
        val type = selectedformat.value

        when (type) {
            "Ground Truth" -> controller.saveToGroundTruth()
            "KML" -> controller.saveToKml()
        }
    }

    private fun convertDirectory() {
        val type = selectedformat.value

        when (type) {
            "Ground Truth" -> controller.convertDirectoryToGroundTruth()
            "KML" -> controller.convertDirectoryToKML()
        }
    }

    private fun openFile() {
        controller.readFile()

        preview.text = controller.text
        baroAlt.items = controller.baroAlt
        gga.items = controller.gga
        rma.items = controller.rmc
        groundTruth.items = controller.groundTruth
        kml.text = controller.kmlText

    }
}