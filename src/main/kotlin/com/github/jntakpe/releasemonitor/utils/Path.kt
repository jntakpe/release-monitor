package com.github.jntakpe.releasemonitor.utils

fun String.dotToSlash() = this.replace(".", "/")

fun String.removeLeadingSlash() = this.replaceFirst("/", "")