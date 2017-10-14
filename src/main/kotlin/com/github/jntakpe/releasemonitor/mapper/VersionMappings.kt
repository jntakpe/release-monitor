package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.VersionType
import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.utils.removeLeadingSlash
import com.github.zafarkhaja.semver.Version

fun Folder.toRawVersions() = this.children.map { it.uri }.map { it.removeLeadingSlash() }

fun String.toAppVersion(): AppVersion {
    val semver = Version.valueOf(this)
    val (type, rcNumber) = semver.preReleaseVersion.toVersionType()
    return AppVersion(this, semver.majorVersion, semver.minorVersion, semver.patchVersion, type, rcNumber)
}

private fun String.toVersionType() = when {
    this.equals("RELEASE", true) || this.isEmpty() -> VersionType.RELEASE to null
    this.startsWith("RC", true) -> VersionType.RELEASE_CANDIDATE to this.filter { it.isDigit() }.toInt()
    this.equals("SNAPSHOT", true) -> VersionType.SNAPSHOT to null
    else -> throw IllegalStateException("Unable to parse version type $this")
}
