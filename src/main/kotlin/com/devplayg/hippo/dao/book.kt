package com.devplayg.hippo.dao


//
//object BookzTable : UUIDCrudTable("bookz") {
//    val id = uuid("id")
//    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "bookz_pkey")
//    val createdAt = instant("created_at")
//    val modifiedAt = instant("updated_at")
//    val isActive = bool("is_active")
//    val data = jsonb("data", BookzData::class.java, jacksonObjectMapper())
//
//    override val crudIdColumn: () -> Column<UUID> = { id }
//}
//
//data class BookzRecord(
//        val id: UUID, val createdAt: Instant, val modifiedAt: Instant,
//        val isActive: Boolean,
//        val data: BookzData
//)
//
//data class BookzData(val title: String, val genres: List<String>, val published: Boolean)
//
//fun BookzRecord.crudRecordId(): UUID = id
//
//fun BookzTable.rowToBookzRecord(row: ResultRow): BookzRecord = BookzRecord(
//        id = row[id],
//        createdAt = row[createdAt],
//        modifiedAt = row[modifiedAt],
//        isActive = row[isActive],
//        data = row[data]
//)
//
//fun ResultRow.toBookzRecord(): BookzRecord = BookzTable.rowToBookzRecord(this)
