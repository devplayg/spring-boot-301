package com.devplayg.hippo

import com.devplayg.hippo.config.DatabaseInitializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class HippoApplication

fun main(args: Array<String>) {
    runApplication<HippoApplication>(*args)
    DatabaseInitializer.init()
}
