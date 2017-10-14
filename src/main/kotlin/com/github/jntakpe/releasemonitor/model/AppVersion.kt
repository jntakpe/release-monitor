package com.github.jntakpe.releasemonitor.model

data class AppVersion(val raw: String, val major: Int, val minor: Int, val patch: Int, val type: VersionType, val rcNumber: Int? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppVersion) return false

        if (raw != other.raw) return false

        return true
    }

    override fun hashCode(): Int {
        return raw.hashCode()
    }

    override fun toString(): String {
        return "AppVersion(raw='$raw')"
    }

}