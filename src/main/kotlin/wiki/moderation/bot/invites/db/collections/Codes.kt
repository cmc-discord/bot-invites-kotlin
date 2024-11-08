/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.collections

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import wiki.moderation.bot.invites.db.Database
import wiki.moderation.bot.invites.db.entities.CodeEntity
import wiki.moderation.bot.invites.db.tables.CodeTable
import java.util.UUID

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

	suspend fun read(id: UUID): CodeEntity? = Database.transaction {
		CodeTable.selectAll()
			.where { CodeTable.id eq id }
			.map { CodeEntity.fromRow(it) }
			.singleOrNull()
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
