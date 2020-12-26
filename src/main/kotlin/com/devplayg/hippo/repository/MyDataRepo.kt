package com.devplayg.hippo.repository

import org.springframework.stereotype.Repository

//@Repository
//class MyDataRepo {
//    //
//    /**
//     * 전체 조회
//     */
//    fun findAll(filter: MyDataFilter) = transaction {
//        predicate()
//            .orderBy(getSortOrder(MyData, filter))
//            .map {
//                toMyData(it)
//            }
//    }
//
//
//    /**
//     * 선택 조회
//     */
//    @Throws(Exception::class)
//    fun findById(id: Int) = transaction {
//        predicate()
//            .andWhere { MyData.id eq id }
//            .map { toMyData(it) } // returns a standard Kotlin List
//            .singleOrNull()
//    }
//
//
//    /**
//     * 등록
//     */
//    @Throws(Exception::class)
//    fun create(filter: MyDataFilter, _memberId: Long) = transaction {
//        val now = DateTime.now()
//        MyData.insert {
//            it[updated] = now
//        } get MyData.id
//    }
//
//
//    /**
//     * 변경
//     */
//    @Throws(Exception::class)
//    fun update(id: Int, filter: MyDataFilter, _memberId: Long) = transaction {
//        val _old = findById(id) ?: return@transaction 0
//        val affectedRows = MyData.update({ MyData.id.eq(id) }) {
//        }
//
//        val _new = findById(id)
//        if (_new!!.changed(_old)) { // 정책이 변경여부
//        }
//
//        affectedRows
//    }
//
//
//    /**
//     * 변경
//     */
//    @Throws(Exception::class)
//    fun delete(id: Int, _memberId: Long) = transaction {
//        MyData.deleteWhere { MyData.id.eq(id) }
//    }
//
//}