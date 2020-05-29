package com.devplayg.hippo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class AppConfig(
        var name: String = "",
        var version: String = "",
        var whatElse: String = "",
        var pathPatternsNotToBeIntercepted: ArrayList<String> = ArrayList()
)
