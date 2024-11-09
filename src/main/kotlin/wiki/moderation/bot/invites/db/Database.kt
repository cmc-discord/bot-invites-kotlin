/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.kordex.core.utils.envOrNull
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import wiki.moderation.bot.invites.DB_DRIVER
import wiki.moderation.bot.invites.DB_PASSWORD
import wiki.moderation.bot.invites.DB_URL
import wiki.moderation.bot.invites.DB_USER
import wiki.moderation.bot.invites.db.Database.db
import wiki.moderation.bot.invites.db.tables.ApplicationTable
import wiki.moderation.bot.invites.db.tables.CodeTable
import wiki.moderation.bot.invites.db.tables.UserTable
import org.jetbrains.exposed.sql.Database as ExposedDB

@Suppress("MagicNumber")
object Database {
	private val hikariConfig by lazy {
		HikariConfig().apply {
			driverClassName = DB_DRIVER

			jdbcUrl = "jdbc:$DB_URL"
			username = DB_USER
			password = DB_PASSWORD

			isReadOnly = false
			maximumPoolSize = 6
			transactionIsolation = "TRANSACTION_SERIALIZABLE"
		}
	}

	private val dataSource by lazy { HikariDataSource(hikariConfig) }

	val db by lazy {
		ExposedDB.connect(
			dataSource,

			databaseConfig = DatabaseConfig {
			}
		)
	}

	init {
		val flyway = Flyway.configure()
			.driver(DB_DRIVER)
			.dataSource("jdbc:$DB_URL", DB_USER, DB_PASSWORD)
			.validateMigrationNaming(true)
			.load()

		if (envOrNull("SKIP_MIGRATIONS") == null) {
			flyway.migrate()
		}
	}

	suspend fun <T> transaction(body: suspend Transaction.() -> T) =
		newSuspendedTransaction(Dispatchers.IO, db, statement = body)

	fun <T> syncTransaction(body: Transaction.() -> T) =
		transaction(db, statement = body)
}

fun main() {
	Database.syncTransaction {
		println()
		println("== Statements: Applications ==")
		println()

		SchemaUtils
			.createStatements(ApplicationTable)
			.map { "$it;" }
			.forEach(::println)

		println()
		println("== Statements: Codes ==")
		println()

		SchemaUtils
			.createStatements(CodeTable)
			.map { "$it;" }
			.forEach(::println)

		println()
		println("== Statements: Users ==")
		println()

		SchemaUtils
			.createStatements(UserTable)
			.map { "$it;" }
			.forEach(::println)
	}
}
