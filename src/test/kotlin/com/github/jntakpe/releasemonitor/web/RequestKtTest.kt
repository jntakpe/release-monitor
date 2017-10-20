package com.github.jntakpe.releasemonitor.web

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test
import org.springframework.mock.web.reactive.function.server.MockServerRequest

class RequestKtTest {

    @Test
    fun `idFromPath should retrieve id`() {
        val id = ObjectId()
        val request = MockServerRequest.builder().pathVariable("id", id.toString()).build()
        assertThat(request.idFromPath()).isEqualTo(id)
    }

}