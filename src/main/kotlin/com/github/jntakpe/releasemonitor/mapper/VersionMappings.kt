package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.VersionType
import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.utils.removeLeadingSlash
import com.github.zafarkhaja.semver.Version

fun Folder.toRawVersions() = this.children.map { it.uri }.map { it.removeLeadingSlash() }

fun String.toAppVersion(): AppVersion {
    val semver = Version.valueOf(this)
    val versionType = semver.preReleaseVersion.toVersionType()
    val rcNumber = if (versionType == VersionType.RELEASE_CANDIDATE) semver.preReleaseVersion.toRCNumber() else null
    return AppVersion(this, semver.majorVersion, semver.minorVersion, semver.patchVersion, versionType, rcNumber)
}

private fun String.toVersionType() = when {
    this == "RELEASE" || this.isEmpty() -> VersionType.RELEASE
    this.startsWith("RC") -> VersionType.RELEASE_CANDIDATE
    this == "SNAPSHOT" -> VersionType.SNAPSHOT
    else -> throw IllegalStateException("Unable to parse version type $this")
}

private fun String.toRCNumber() = this.removePrefix("RC").toInt()
