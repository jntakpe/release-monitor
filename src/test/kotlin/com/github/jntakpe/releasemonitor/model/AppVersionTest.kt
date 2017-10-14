package com.github.jntakpe.releasemonitor.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AppVersionTest {

    @Test
    fun `compareTo should be zero when same versions`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy())).isZero()
    }

    @Test
    fun `compareTo should be zero when same versions but raw changes`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "1.2.3-RELEASE"))).isZero()
    }

    @Test
    fun `compareTo should be positive when major greater`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "0.2.3", major = 0))).isPositive()
    }

    @Test
    fun `compareTo should be negative when major leaser`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "2.2.3", major = 2))).isNegative()
    }

    @Test
    fun `compareTo should be positive when minor greater`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "1.1.3", minor = 1))).isPositive()
    }

    @Test
    fun `compareTo should be negative when minor leaser`() {
        val version = AppVersion("1.1.3", 1, 1, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "1.2.3", minor = 2))).isNegative()
    }

    @Test
    fun `compareTo should be positive when patch greater`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "1.2.2", patch = 2))).isPositive()
    }

    @Test
    fun `compareTo should be negative when patch leaser`() {
        val version = AppVersion("1.2.2", 1, 2, 2, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "1.2.3", patch = 3))).isNegative()
    }

    @Test
    fun `compareTo should be positive when release comparing to snapshot`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "1.2.3-SNAPSHOT", type = VersionType.SNAPSHOT))).isPositive()
    }

    @Test
    fun `compareTo should be positive when release comparing to RC`() {
        val version = AppVersion("1.2.3", 1, 2, 3, VersionType.RELEASE)
        assertThat(version.compareTo(version.copy(raw = "1.2.3-RC1", type = VersionType.RELEASE_CANDIDATE))).isPositive()
    }

    @Test
    fun `compareTo should be negative when snapshot comparing to RC`() {
        val version = AppVersion("1.2.3-SNAPSHOT", 1, 2, 3, VersionType.SNAPSHOT)
        assertThat(version.compareTo(version.copy(raw = "1.2.3-RC1", type = VersionType.RELEASE_CANDIDATE))).isNegative()
    }

    @Test
    fun `compareTo should be negative when snapshot comparing to release`() {
        val version = AppVersion("1.2.3-SNAPSHOT", 1, 2, 3, VersionType.SNAPSHOT)
        assertThat(version.compareTo(version.copy(raw = "1.2.3", type = VersionType.RELEASE))).isNegative()
    }

    @Test
    fun `compareTo should be positive when RC comparing to snapshot`() {
        val version = AppVersion("1.2.3-RC1", 1, 2, 3, VersionType.RELEASE_CANDIDATE)
        assertThat(version.compareTo(version.copy(raw = "1.2.3-SNAPSHOT", type = VersionType.SNAPSHOT))).isPositive()
    }

    @Test
    fun `compareTo should be negative when RC comparing to release`() {
        val version = AppVersion("1.2.3-RC1", 1, 2, 3, VersionType.RELEASE_CANDIDATE)
        assertThat(version.compareTo(version.copy(raw = "1.2.3", type = VersionType.RELEASE))).isNegative()
    }

    @Test
    fun `compareTo should be equals when RC has same number`() {
        val version = AppVersion("1.2.3-RC1", 1, 2, 3, VersionType.RELEASE_CANDIDATE, rcNumber = 1)
        assertThat(version.compareTo(version.copy())).isZero()
    }

    @Test
    fun `compareTo should be positive when RC number greater`() {
        val version = AppVersion("1.2.3-RC3", 1, 2, 3, VersionType.RELEASE_CANDIDATE, rcNumber = 3)
        assertThat(version.compareTo(version.copy(raw = "1.2.3-RC1", rcNumber = 1))).isPositive()
    }

    @Test
    fun `compareTo should be negative when RC number leaser`() {
        val version = AppVersion("1.2.3-RC1", 1, 2, 3, VersionType.RELEASE_CANDIDATE, rcNumber = 1)
        assertThat(version.compareTo(version.copy(raw = "1.2.3-RC3", rcNumber = 3))).isNegative()
    }
}