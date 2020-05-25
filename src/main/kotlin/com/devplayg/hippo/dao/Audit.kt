package com.devplayg.hippo.dao

import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
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

data class AuditDto(
        val id: Long,
        val ip: Long,
        val message: String?,
        val created: String
)


//class Audit(id: Audits.) : LongEntity(id) {
////    companion object : LongEntityClass<Audit>(Audits)
//
//    var message by Audits.message
//    var created by Audits.created
//}


//object BookTable : Table("book") {
//    val id = uuid("id")
//    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "book_pkey")
//    val createdAt = instant("created_at")
//    val modifiedAt = instant("updated_at")
//    val version = integer("version")
//    val authorId = (uuid("author_id") references AuthorTable.id)
//    val title = varchar("title", 255)
//    val status = enumerationByName("status", 255, BookStatus::class)
//    val price = decimal("price", 15, 2)
//}
//
//data class BookRecord(
//        val id: UUID, val createdAt: Instant, val modifiedAt: Instant, val version: Int,
//        val authorId: UUID,
//        val title: String, val status: BookStatus, val price: BigDecimal
//)
//
//enum class BookStatus { NEW, PUBLISHED; }
//
//fun BookTable.rowToBookRecord(row: ResultRow): BookRecord =
//        BookRecord(
//                id = row[id], createdAt = row[createdAt], modifiedAt = row[modifiedAt], version = row[version],
//                authorId = row[authorId],
//                title = row[title], status = row[status], price = row[price]
//        )
