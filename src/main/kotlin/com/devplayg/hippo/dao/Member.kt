package com.devplayg.hippo.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object Members : IntIdTable("mbr_members") {
    val name = varchar("name", 50).index()
    val age = integer("age")
}
class Member(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Member>(Members)

    var name by Members.name
    var age by Members.age
}


//
//object Users : IntIdTable() {
//    val name = varchar("name", 50).index()
//    val password = varchar("password", 64)
//}

//object Users : IntIdTable("mbr_member") {
//    val id = integer("member_id") // Column<String>
//    val name = varchar("name", length = 32) // Column<String>
//    val password = varchar("password", length = 64) // Column<String>
//
//    override val primaryKey = PrimaryKey(id, name = "PK_User_ID") // name is optional here
//}
//
//
//class User(memberId: EntityID<Int>) : IntEntity(memberId) {
//    companion object : IntEntityClass<User>(Users)
//
//    var name by Users.name
//    var password by Users.password
//}
