package com.wintec.blackbox.model

abstract class Parser(val regex: Regex) {
    companion object {
        val nmeaSentance = """${'$'}?GP[a-z]{3,},([a-z0-9.]*,)+([a-z0-9]{1,2}\*[a-z0-9]{1,2})""".toRegex()
    }
}