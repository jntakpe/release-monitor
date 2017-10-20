package com.github.jntakpe.releasemonitor.web

import org.bson.types.ObjectId
import org.springframework.web.reactive.function.server.ServerRequest

fun ServerRequest.idFromPath() = ObjectId(pathVariable("id"))