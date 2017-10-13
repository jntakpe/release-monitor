package com.github.jntakpe.releasemonitor.mapper

import com.github.jntakpe.releasemonitor.model.client.Folder
import com.github.jntakpe.releasemonitor.utils.removeLeadingSlash

fun Folder.toVersions() = this.children.map { it.uri }.map { it.removeLeadingSlash() }