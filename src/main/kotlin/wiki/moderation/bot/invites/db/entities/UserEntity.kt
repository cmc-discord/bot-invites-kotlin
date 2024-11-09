/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.entities

import dev.kord.common.entity.Snowflake
import dev.kordex.data.api.serializers.KXUUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import wiki.moderation.bot.invites.db.collections.Users
import wiki.moderation.bot.invites.db.tables.UserTable
import java.util.UUID

@Serializable
@Suppress("DataClassContainsFunctions", "DataClassShouldBeImmutable")
data class UserEntity(
	val id: Snowflake,

	@Serializable(with = KXUUIDSerializer::class)
	var inviteCode: UUID? = null,

	var lastJoined: LocalDateTime,
	var lastSeen: LocalDateTime,

	var codesRemaining: Int = 0,
) {
	suspend fun save() =
		Users.upsert(this)

	fun toStatement(statement: UpdateBuilder<*>) {
		with(UserTable) {
			statement[id] = this@UserEntity.id.value
			statement[inviteCode] = this@UserEntity.inviteCode
			statement[lastJoined] = this@UserEntity.lastJoined
			statement[lastSeen] = this@UserEntity.lastSeen
			statement[codesRemaining] = this@UserEntity.codesRemaining
		}
	}

	companion object {
		fun fromRow(row: ResultRow): UserEntity =
			with(UserTable) {
				UserEntity(
					id = Snowflake(row[id].value),
					inviteCode = row[inviteCode],
					lastJoined = row[lastJoined],
					lastSeen = row[lastSeen],
					codesRemaining = row[codesRemaining],
				)
			}
	}
}
