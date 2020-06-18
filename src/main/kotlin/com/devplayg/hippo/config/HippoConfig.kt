package com.devplayg.hippo.config

import com.devplayg.hippo.entity.Members
import com.devplayg.hippo.entity.toJson
import com.devplayg.hippo.entity.toMemberDto
import com.devplayg.hippo.repository.MemberCacheRepo
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HippoConfig {
    @Bean
    fun initDatabase(memberCacheRepo: MemberCacheRepo) = ApplicationRunner {
        Database.connect(HikariDataSource(hikariConfig()))
        transaction {
//            SchemaUtils.create(Members, Audits)
//            Audit.new {
//                memberId = 0
//                category = 3333
//                message = "started"
//                ip = 132331
//            }
//            Member.all().map {
//                memberCacheRepo.save(it)
//            }
            Members.selectAll().map{
                memberCacheRepo.save(toMemberDto(it))
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
