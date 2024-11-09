/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.collections

import dev.kord.common.entity.Snowflake
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import wiki.moderation.bot.invites.db.Database
import wiki.moderation.bot.invites.db.entities.ApplicationEntity
import wiki.moderation.bot.invites.db.tables.ApplicationTable
import wiki.moderation.bot.invites.db.types.ApplicationState
import java.util.UUID

object Applications {
	suspend fun upsert(entity: ApplicationEntity): UUID {
		read(entity.id)
			?: return create(entity)

		update(entity)

		return entity.id
	}

	@Suppress("UnnecessaryParentheses")  // More readable like this!
	suspend fun currentByUser(id: Snowflake): ApplicationEntity? = Database.transaction {
		ApplicationTable.selectAll()
			.where {
				(ApplicationTable.applicant eq id.value) and
					(ApplicationTable.state eq ApplicationState.OPEN)
			}.map { ApplicationEntity.fromRow(it) }
			.singleOrNull()
	}

	suspend fun create(entity: ApplicationEntity): UUID = Database.transaction {
		ApplicationTable.insert {
			entity.toStatement(it)
		}[ApplicationTable.id].value
	}

	suspend fun read(id: UUID): ApplicationEntity? = Database.transaction {
		ApplicationTable.selectAll()
			.where { ApplicationTable.id eq id }
			.map { ApplicationEntity.fromRow(it) }
			.singleOrNull()
	}

	suspend fun update(entity: ApplicationEntity): Int = Database.transaction {
		ApplicationTable.update({ ApplicationTable.id eq entity.id }) {
			entity.toStatement(it)
		}
	}

	suspend fun delete(id: UUID): Int = Database.transaction {
		ApplicationTable.deleteWhere { ApplicationTable.id eq id }
	}
}
