package com.devplayg.hippo

import com.devplayg.hippo.config.AppConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(AppConfig::class)
class HippoApplication

fun main(args: Array<String>) {
    runApplication<HippoApplication>(*args)
}
