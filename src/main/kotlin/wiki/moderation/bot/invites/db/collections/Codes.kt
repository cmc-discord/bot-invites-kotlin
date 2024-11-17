/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.collections

import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import wiki.moderation.bot.invites.db.Database
import wiki.moderation.bot.invites.db.entities.CodeEntity
import wiki.moderation.bot.invites.db.entities.UserEntity
import wiki.moderation.bot.invites.db.tables.CodeTable
import java.util.UUID

typealias CodeEntityFilter = (CodeEntity) -> Boolean
val defaultFilter: CodeEntityFilter = { true }

object Codes {
	suspend fun upsert(entity: CodeEntity): UUID {
		read(entity.id)
			?: return create(entity)

		update(entity)

		return entity.id
	}

	suspend fun create(entity: CodeEntity): UUID = Database.transaction {
		CodeTable.insert {
			entity.toStatement(it)
		}[CodeTable.id].value
	}

	suspend fun create(user: UserEntity, note: String?): CodeEntity = Database.transaction {
		val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

		val codeEntity = CodeEntity(
			id = UUID.randomUUID(),
			ownedBy = user.id,
			createdAt = now,
			note = note
		)

		CodeTable.insert {
			codeEntity.toStatement(it)
		}

		codeEntity
	}

	suspend fun read(
		id: UUID,
		filter: CodeEntityFilter = defaultFilter
	): CodeEntity? = Database.transaction {
		CodeTable.selectAll()
			.where { CodeTable.id eq id }
			.map { CodeEntity.fromRow(it) }
			.singleOrNull(filter)
	}

	@Suppress("UnnecessaryParentheses")  // More readable like this!
	suspend fun readOwned(
		id: UUID,
		ownerId: Snowflake,
		filter: CodeEntityFilter = defaultFilter
	): CodeEntity? = Database.transaction {
		CodeTable.selectAll()
			.where { (CodeTable.id eq id) and (CodeTable.ownedBy eq ownerId.value) }
			.map { CodeEntity.fromRow(it) }
			.singleOrNull(filter)
	}

	suspend fun getForOwner(
		ownerId: Snowflake,
		filter: CodeEntityFilter = defaultFilter
	): List<CodeEntity> = Database.transaction {
		CodeTable.selectAll()
			.where { CodeTable.ownedBy eq ownerId.value }
			.map { CodeEntity.fromRow(it) }
			.filter(filter)
	}

	suspend fun update(entity: CodeEntity): Int = Database.transaction {
		CodeTable.update({ CodeTable.id eq entity.id }) {
			entity.toStatement(it)
		}
	}

	suspend fun delete(id: UUID): Int = Database.transaction {
		CodeTable.deleteWhere { CodeTable.id eq id }
	}
}
