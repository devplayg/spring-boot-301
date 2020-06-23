package com.devplayg.hippo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app")
data class AppConfig(
        val version: String = "",
        val homeUri: String = "",
        val pathPatternsNotToBeIntercepted: ArrayList<String> = ArrayList(),
        val dataSource: DataSource
) {
    data class DataSource(val url: String = "", val username: String = "", val password: String = "")
}
