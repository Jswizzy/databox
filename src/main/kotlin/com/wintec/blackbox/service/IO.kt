package com.wintec.inu.service


import javafx.stage.FileChooser.ExtensionFilter
import tornadofx.*
import java.io.File
import java.io.FileFilter


/**
 * IO Controller for saving and opening files
 */
class IO: Controller() {

    /**
     * Save File
     * if null do nothing
     */
    fun textToFile(file: File?, string: String) {

        if (file != null) {
            val name = nameFile(file)
            val outFile = File(name)

            outFile.printWriter().use { out ->
                string.split("\n").forEach {
                    out.println(it)
                }
            }
        }
    }


    /**
     * Save a directory given a function that converts a file to a string
     */
    fun convertDirectory(fileToTextConverter: (File) -> String) {
        val directory = chooseDirectory("Choose directory")

        directory?.listFiles(
                FileFilter { it.extension == "TXT" })
                ?.forEach {
                    val text = fileToTextConverter(it)
                    textToFile(it, text)
                }
    }

    /**
     * Save a directory given a function that takes a file and saves it
     */
    fun saveDirectory(fileConverter: (File) -> Unit) {
        val directory = chooseDirectory("Choose directory")

        directory?.listFiles(
                FileFilter { it.extension == "TXT" })
                ?.forEach {
                    fileConverter(it)
                }
    }

    /**
     * Open Files
     */
    fun openFile()  =
            chooseFile(
                    "select file",
                    arrayOf(ExtensionFilter(
                            "txt, nmea",
                            "*.txt", "*.nmea")))
            .firstOrNull()

    /**
     * Helper class to rename files to <orginal path>-copy.txt
     */
    fun nameFile(file: File?) = "${file?.path?.dropLast(4)}-copy.txt"
}
