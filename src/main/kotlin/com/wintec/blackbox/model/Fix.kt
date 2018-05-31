package com.wintec.blackbox.model

enum class Fix(val quality: Int) {
    INVALID(0),
    GPS_FIX(1),
    DGPS_FIX(2);

    override fun toString(): String {
        return quality.toString()
    }

    companion object {
        fun get(quality: Int): Fix {
            return when(quality) {
                0 -> INVALID
                1 -> GPS_FIX
                2 -> DGPS_FIX
                else -> throw IllegalArgumentException()
            }
        }
    }
}