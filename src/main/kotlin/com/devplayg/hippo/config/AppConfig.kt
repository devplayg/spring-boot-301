package com.devplayg.hippo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties("app")
data class AppConfig(
        val name: String = "",
        val homeUri: String = "",
        val version: String = "",
        val maxUserSessions: Int = 5,
        val revokeInactiveUsersRoleAfterDays: Int = 30,
        val clientSessionTimeoutSeconds: Int = 0,
        val pathPatternsNotToBeIntercepted: ArrayList<String> = ArrayList(),
        val dataSource: DataSource,
        val emailDomain: String,
        val ldap: Ldap?
) {


        data class Ldap(
                val userSearchFilter: String = "",
                val url: String = "",
                val port: String = "",
                val userDn: String = "",
                val managerDn: String = "",
                val managerPassword: String = "",
                val groupSearchBase: String = ""
        )

        /**
         * DataSource
         */
        data class DataSource(
                val driverClassName: String = "",
                val jdbcUrl: String = "",
                val user: String = "",
                val password: String = "",
                val portNumber: String = "",
                val serverName: String = "",
                val minimumIdle: Int,
                val maximumPoolSize: Int
        )
}