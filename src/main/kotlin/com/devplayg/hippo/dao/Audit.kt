package com.devplayg.hippo.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object Audits : Table("adt_audit") {
    val id = long("audit_id").autoIncrement()
    val ip = long("ip")
    val message = varchar("message", 256).nullable()
    val created = datetime("created")

    override val primaryKey = PrimaryKey(id, name= "PK_adt_audit_auditId")
}

data class Audit(
        val id: Long,
        val ip: Long,
        val message: String?,
        val created: String
)


fun Audits.toAudit(row: ResultRow) =
        Audit(
                id = row[id],
                ip = row[ip],
                message = row[message],
                created = row[created].toString()
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
