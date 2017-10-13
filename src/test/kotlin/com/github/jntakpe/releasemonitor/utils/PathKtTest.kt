package com.github.jntakpe.releasemonitor.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PathKtTest {

    @Test
    fun `dotToSlash should replace single dot`() {
        assertThat("com.github".dotToSlash()).isEqualTo("com/github")
    }

    @Test
    fun `dotToSlash should replace multiple dots`() {
        assertThat("com.github.jntakpe".dotToSlash()).isEqualTo("com/github/jntakpe")
    }

    @Test
    fun `dotToSlash should replace none`() {
        assertThat("com".dotToSlash()).isEqualTo("com")
    }

    @Test
    fun `removeLeadingSlash should remove slash`() {
        assertThat("/0.1.0".removeLeadingSlash()).isEqualTo("0.1.0")
    }

    @Test
    fun `removeLeadingSlash should remove first slash`() {
        assertThat("/0.1.0/".removeLeadingSlash()).isEqualTo("0.1.0/")
    }

    @Test
    fun `removeLeadingSlash should do nothing`() {
        assertThat("0.1.0".removeLeadingSlash()).isEqualTo("0.1.0")
    }

}