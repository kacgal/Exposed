package org.jetbrains.exposed.sql.vendors

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal class MariadbDialect : MysqlDialect(dialectName) {

    override fun tableColumns(vararg tables: Table): Map<Table, List<Pair<String, Boolean>>> {
        return TransactionManager.current().exec(
                "SELECT DISTINCT TABLE_NAME, COLUMN_NAME, IS_NULLABLE FROM" +
                        " INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '${getDatabase()}'") { rs ->
            rs.extractColumns(tables) {
                Triple(it.getString("TABLE_NAME")!!, it.getString("COLUMN_NAME")!!, it.getString("IS_NULLABLE") == "YES")
            }
        }!!
    }

    companion object {
        const val dialectName = "mariadb"
    }
}