/**
 * 데이터 조회결과가 저장될 데이터 클래스
 */
data class PageData(val rows: Any, val total: Long)

/**
 * getSortOrder
 */
fun getSortOrder(table: MapperTable, filter: Filter): Pair<Expression<*>, SortOrder> {
    val colName = table.mapper().getOrDefault(filter.sort, filter.sort)
    if (table is LongIdTable) {
        // LongIdTable?
        return getLongIdTableSortOrder(table, colName, filter.order)
    }

    // InitIdTable
    return getInitIdTableSortOrder((table as IntIdTable), colName, filter.order)
}


/**
 * Long타입 컬럼을 PK로 사용하는 테이블
 */
fun getLongIdTableSortOrder(tb: LongIdTable, colName: String, order: String): Pair<Expression<*>, SortOrder> {
    val col = tb.columns.firstOrNull {
        it.name == colName
    } ?: tb.id
    return Pair(col, toSortOrder(order))
}


/**
 * Int타입 컬럼을 PK로 사용하는 테이블
 */
fun getInitIdTableSortOrder(tb: IntIdTable, colName: String, order: String): Pair<Expression<*>, SortOrder> {
    val col = tb.columns.firstOrNull {
        it.name == colName
    } ?: tb.id
    return Pair(col, toSortOrder(order))
}



