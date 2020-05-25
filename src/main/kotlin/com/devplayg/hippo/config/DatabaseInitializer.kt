package com.devplayg.hippo.config

import com.devplayg.hippo.entity.Audits
import com.devplayg.hippo.entity.Member
import com.devplayg.hippo.entity.Members
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.withContext


object DatabaseInitializer {
    fun init() {
        Database.connect(HikariDataSource(hikariConfig()))
        transaction {
            create(Members, Audits)

            Member.new {
                name = "a"
                age = 3
            }
        }
    }
}

private fun hikariConfig() =
        HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"
            jdbcUrl = "jdbc:mysql://127.0.0.1:3306/sb201?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC"
            maximumPoolSize = 10
            minimumIdle = 10
            isAutoCommit = false
            username = "root"
            password = "Uniiot12!@"
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

// Database.connect("", driver = "", user = "root", password = "Uniiot12!@")
suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction {
        block()
    }
}
