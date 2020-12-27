package com.devplayg.hippo.repository

import com.devplayg.hippo.entity.ConfigDto
import com.devplayg.hippo.entity.Configs
import com.devplayg.hippo.entity.toConfigDto
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository

@Repository
class ConfigRepo {

    fun findAll() = transaction {
        Configs.selectAll()
                .map {
                    toConfigDto(it)
                }
    }


    @Throws(Exception::class)
    fun findBySectionAndKeyword(section: String, keyword: String) = transaction {
        Configs.select() {
            Configs.section.eq(section) and  Configs.keyword.eq(keyword)
        }.map {
            toConfigDto(it)
        }.firstOrNull()

    }

    @Throws(Exception::class)
    fun findBySections(vararg sections: String) = transaction {
        Configs.select {
            Configs.section.inList(sections.asList())
        }.map {
            toConfigDto(it)
        }
    }


    fun updateOne(dto: ConfigDto) = transaction {
        Configs.update ({
            (Configs.section eq dto.section) and (Configs.keyword eq dto.keyword)
        }) {
            it[Configs.str1] = dto.str1
            it[Configs.str2] = ""
            it[Configs.num1] = 0
            it[Configs.num2] = 0
        }
    }

}
