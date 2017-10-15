package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.Application
import com.github.jntakpe.releasemonitor.model.api.ApplicationDTO
import org.bson.types.ObjectId

fun ApplicationDTO.toEntity() = Application(group, name, versions, id?.let { ObjectId(it) })

fun Application.toDTO() = ApplicationDTO(group, name, versions, id?.toString())