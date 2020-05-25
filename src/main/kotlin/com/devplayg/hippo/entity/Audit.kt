package com.devplayg.hippo.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

// Table
object Audits : LongIdTable("adt_audit", "audit_id") {
    val category = integer("category").index()
    val ip = long("ip")
    val message = varchar("message", 256).nullable()
    val created = datetime("created")

    override val primaryKey = PrimaryKey(id, name = "PK_adt_audit_auditId")
}

// Entity
class Audit(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Audit>(Audits)

    var category by Audits.category
    var ip by Audits.ip
    var message by Audits.message
    var created by Audits.created
    fun toDto() = AuditDto(
            id = this.id.value,
            ip = this.ip,
            message = this.message,
            created = this.created.toString()
    )
}

// DTO
data class AuditDto(
        val id: Long,
        val ip: Long,
        val message: String?,
        val created: String
)
