package com.github.jntakpe.releasemonitor.model.api

import com.github.jntakpe.releasemonitor.model.AppVersion

data class ApplicationDTO(val group: String, val name: String, val versions: List<AppVersion> = listOf(), val id: String? = null)