


object Assets : LongIdTable("ast_asset", "asset_id"), MapperTable {
    val parentId = long("parent_id")
    val type = integer("type")
    val category = integer("category")
    val name = varchar("name", 64)
    val code = varchar("code", 16)
    val seq = integer("seq")

    override val primaryKey = PrimaryKey(id)

    override fun mapper() = hashMapOf(
            "assetId" to "asset_id",
            "seq" to "sequence",
            "parentId" to "parent_id"
    )
}

data class AssetDto(
        val id: Long?,
        val parentId: Long,
        var name: String,
        val code: String,
        val type: Int,
        val category: Int,
        val sequence: Int
)
fun AssetDto.json(): String = Gson().toJson(this)

fun toAssetDto(it: ResultRow): AssetDto {
    return AssetDto(
            id = it[Assets.id].value,
            parentId = it[Assets.parentId],
            name = it[Assets.name],
            code = it[Assets.code],
            type = it[Assets.type],
            category = it[Assets.category],
            sequence = it[Assets.seq]
    )
}
