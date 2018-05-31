package com.wintec.blackbox.model


object LoggerId : Parser(
        """LOGGERID,(\d*),.*""".toRegex()
) {
    fun from(string: String): Int {

        val id = regex.matchEntire(string)?.groups?.get(1)?.value

        return id?.toIntOrNull() ?: -1
    }

    fun fromText(text: String): Int {
        return text.splitToSequence("\n")
                .map {
                    it.drop(1).trim()
                }
                .filter {
                    regex.matches(it)
                }
                .map {
                    from(it)
                }
                .firstOrNull {
                    it != -1
                } ?: -1
    }
}