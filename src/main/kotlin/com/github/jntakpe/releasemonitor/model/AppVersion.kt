package com.github.jntakpe.releasemonitor.model

data class AppVersion(val raw: String, val major: Int, val minor: Int, val patch: Int, val type: VersionType, val rcNumber: Int? = null)
    : Comparable<AppVersion> {

    override fun compareTo(other: AppVersion): Int {
        return compareBy<AppVersion>({ it.major }, { it.minor }, { it.patch }, { it.type }, { it.rcNumber }).compare(this, other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppVersion) return false

        if (major != other.major) return false
        if (minor != other.minor) return false
        if (patch != other.patch) return false
        if (type != other.type) return false
        if (rcNumber != other.rcNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = major
        result = 31 * result + minor
        result = 31 * result + patch
        result = 31 * result + type.hashCode()
        result = 31 * result + (rcNumber ?: 0)
        return result
    }

    override fun toString(): String {
        return "AppVersion(raw='$raw')"
    }

}