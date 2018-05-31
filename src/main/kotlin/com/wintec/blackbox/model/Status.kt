package com.wintec.blackbox.model

enum class Status(val isValid: Boolean) {
    A(true),
    V(false);

    override fun toString(): String {
        return if (this == A) "Valid" else "Invalid"
    }
}