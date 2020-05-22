package com.devplayg.hippo

import com.devplayg.hippo.dao.Audits
import com.devplayg.hippo.dao.Member
import com.devplayg.hippo.dao.Members
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


@SpringBootApplication
class HippoApplication

fun main(args: Array<String>) {
    runApplication<HippoApplication>(*args)

    Database.connect("jdbc:mysql://127.0.0.1:3306/sb201?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC", driver = "com.mysql.cj.jdbc.Driver", user = "root", password = "Uniiot12!@")
    transaction {
        SchemaUtils.create(Members, Audits)
        Member.new {
            name = "a"
            age = 3
        }
    }
}
