@Repository
class ConfigRepo {

    fun findAll() = transaction {
        Configs.selectAll()
                .map {
                    toConfigDto(it)
                }
    }


    @Throws(Exception::class)
    fun findBySectionAndKeyword(section: String, keyword: String) = transaction {
        Configs.select {
            Configs.section.eq(section) and Configs.keyword.eq(keyword)
        }.map {
            toConfigDto(it)
        }.firstOrNull()

    }

    @Throws(Exception::class)
    fun findBySections(vararg sections: String) = transaction {
        Configs.select {
            Configs.section.inList(sections.asList())
        }.map {
            toConfigDto(it)
        }
    }


    fun updateOne(dto: ConfigDto) = transaction {
        Configs.update({
            (Configs.section eq dto.section) and (Configs.keyword eq dto.keyword)
        }) {
            it[Configs.str1] = dto.str1
            it[Configs.str2] = ""
            it[Configs.num1] = 0
            it[Configs.num2] = 0
        }
    }

}
