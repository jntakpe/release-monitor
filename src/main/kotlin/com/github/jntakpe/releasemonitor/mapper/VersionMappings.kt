package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.AppVersion
import com.github.jntakpe.releasemonitor.model.VersionType
import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.utils.removeLeadingSlash
import com.github.zafarkhaja.semver.Version

fun Folder.toRawVersions() = children.map { it.uri }.map { it.removeLeadingSlash() }

fun String.toAppVersion(): AppVersion {
    val semver = Version.valueOf(this)
    val (type, rcNumber) = semver.preReleaseVersion.toVersionType()
    return AppVersion(this, semver.majorVersion, semver.minorVersion, semver.patchVersion, type, rcNumber)
}

private fun String.toVersionType() = when {
    equals("RELEASE", true) || isEmpty() -> VersionType.RELEASE to null
    startsWith("RC", true) -> VersionType.RELEASE_CANDIDATE to filter { it.isDigit() }.toInt()
    equals("SNAPSHOT", true) -> VersionType.SNAPSHOT to null
    else -> throw IllegalStateException("Unable to parse version type $this")
}
