package com.github.jntakpe.releasemonitor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ReleaseMonitorApplication

fun main(args: Array<String>) {
    SpringApplication.run(ReleaseMonitorApplication::class.java, *args)
}
