/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.collections

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.UserBehavior
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import wiki.moderation.bot.invites.GUILD_ID
import wiki.moderation.bot.invites.db.Database
import wiki.moderation.bot.invites.db.entities.UserEntity
import wiki.moderation.bot.invites.db.tables.UserTable
import java.util.UUID

object Users {
	suspend fun upsert(entity: UserEntity): Snowflake {
		read(entity.id)
			?: return Snowflake(create(entity))

		update(entity)

		return entity.id
	}

	suspend fun create(entity: UserEntity): ULong = Database.transaction {
		UserTable.insert {
			entity.toStatement(it)
		}[UserTable.id].value
	}

	suspend fun getOrCreate(user: UserBehavior, inviteCode: UUID? = null): UserEntity = Database.transaction {
		var entity = read(user.id)

		if (entity == null) {
			val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

			val lastJoined = user.asMemberOrNull(GUILD_ID)?.joinedAt?.toLocalDateTime(TimeZone.UTC)
				?: now

			entity = UserEntity(user.id, inviteCode = inviteCode, lastJoined = lastJoined, lastSeen = now)

			create(entity)
		}

		entity
	}

	suspend fun read(id: Snowflake): UserEntity? =
		read(id.value)

	suspend fun read(id: ULong): UserEntity? = Database.transaction {
		UserTable.selectAll()
			.where { UserTable.id eq id }
			.map { UserEntity.fromRow(it) }
			.singleOrNull()
	}

	suspend fun update(entity: UserEntity): Int = Database.transaction {
		UserTable.update({ UserTable.id eq entity.id.value }) {
			entity.toStatement(it)
		}
	}

	suspend fun delete(id: ULong): Int = Database.transaction {
		UserTable.deleteWhere { UserTable.id eq id }
	}
}
