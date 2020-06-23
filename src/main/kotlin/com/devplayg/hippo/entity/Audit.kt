package com.devplayg.hippo.entity

import com.devplayg.hippo.define.AuditCategory
import com.devplayg.hippo.util.AuditMessage
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.kotlin.com.google.gson.Gson
import org.joda.time.DateTime

// Table
object Audits : LongIdTable("adt_audit", "audit_id") {
    val category = integer("category").index()
    val memberId = long("member_id")
    val ip = long("ip")
    val message = varchar("message", 256).nullable()
    val created = datetime("created").default(DateTime.now())

    override val primaryKey = PrimaryKey(id, name = "PK_adt_audit_auditId")
}

// Entity
class Audit(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Audit>(Audits)

    var memberId by Audits.memberId
    var category by Audits.category
    var ip by Audits.ip
    var message by Audits.message
    var created by Audits.created
    fun toDto() = AuditDto(
            id = this.id.value,
            category = this.category,
            ip = this.ip,
            message = this.message,
            created = this.created.toString()
    )
}

// DTO
data class AuditDto(
        val id: Long,
        val ip: Long,
        val category: Int,
        val message: String?,
        val created: String
)

fun toAuditDto(it: ResultRow) = AuditDto(
        id = it[Audits.id].value,
        ip = it[Audits.ip],
        category = it[Audits.category],
        message = it[Audits.message],
        created = it[Audits.created].toString()
)

