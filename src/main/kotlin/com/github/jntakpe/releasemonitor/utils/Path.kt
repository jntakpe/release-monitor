package com.github.jntakpe.releasemonitor.utils

fun String.dotToSlash() = replace(".", "/")

fun String.removeLeadingSlash() = replaceFirst("/", "")