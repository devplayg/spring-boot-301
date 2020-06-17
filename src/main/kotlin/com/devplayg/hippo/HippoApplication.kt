package com.devplayg.hippo

import com.devplayg.hippo.config.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class HippoApplication

fun main(args: Array<String>) {
    runApplication<HippoApplication>(*args)
}
