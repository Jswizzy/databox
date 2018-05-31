package com.wintec.blackbox.model

data class BaroAlt(val pressure: Double, val temp: Double, val alt: Double) {
    companion object : Parser(
            """BAROALT,(\d*\.?\d*),(\d*\.?\d*),(-?\d*\.?\d*)\*.*"""
                    .toRegex()
    ) {
        fun from(string: String): BaroAlt {

            return try {
                val (pressure, temp, alt) = regex.matchEntire(string)?.destructured ?: throw IllegalArgumentException()

                BaroAlt(
                        pressure.toDouble(),
                        temp.toDouble(),
                        alt.toDouble())
            } catch (e: IllegalArgumentException) {
                BaroAlt(0.0,0.0, 0.0)
            }
        }

        fun read(text: String): List<BaroAlt> {
            return text.splitToSequence("\n")
                    .map {
                        it.drop(1).trim()
                    }
                    .filter {
                        it.matches(regex)
                    }
                    .map {
                        from(it)
                    }
                    .toList()
        }
    }
}
