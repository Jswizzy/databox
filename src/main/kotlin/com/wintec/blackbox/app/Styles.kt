package com.wintec.blackbox.app

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Styles : Stylesheet() {
    companion object {
        val appSizeRule by cssclass()
    }

    init {
        select(appSizeRule) {
            minHeight = 600.px
            minWidth = 960.px
        }
    }
}