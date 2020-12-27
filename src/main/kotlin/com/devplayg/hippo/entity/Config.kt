package com.devplayg.hippo.entity

import com.devplayg.hippo.define.YYYYMMDDHHMMSS
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

/**
 * Table name: sys_config
 * Description: 설정 관리
 */
object Configs : IntIdTable("sys_config", "config_id") {
    val section = varchar("section", 64)
    val keyword = varchar("keyword", 64)
    val str1 = varchar("str1", 2048)
    val str2 = varchar("str2", 2048)
    val num1 = long("num1")
    val num2 = long("num2")
    val enabled = bool("enabled")
    val datetime = datetime("datetime").default(DateTime.now())

    override val primaryKey = PrimaryKey(id)
}

// DTO
data class ConfigDto(
        val id: Int,
        val section: String,
        val keyword: String,
        val str1: String,
        val str2: String,
        val num1: Long,
        val num2: Long,
        val enabled: Boolean,
        val datetime: String
)


fun toConfigDto(it: ResultRow) = ConfigDto(
        id = it[Configs.id].value,
        section = it[Configs.section],
        keyword = it[Configs.keyword],
        str1 = it[Configs.str1],
        str2 = it[Configs.str2],
        num1 = it[Configs.num1],
        num2 = it[Configs.num2],
        enabled = it[Configs.enabled],
        datetime = it[Configs.datetime].toString(YYYYMMDDHHMMSS)
)
