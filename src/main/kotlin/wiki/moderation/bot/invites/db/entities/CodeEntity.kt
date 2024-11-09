/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.entities

import dev.kordex.data.api.serializers.KXUUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import wiki.moderation.bot.invites.db.collections.Codes
import wiki.moderation.bot.invites.db.tables.CodeTable
import java.util.UUID

@Serializable
@Suppress("DataClassContainsFunctions", "DataClassShouldBeImmutable")
data class CodeEntity(
	@Serializable(with = KXUUIDSerializer::class)
	val id: UUID,

	val ownedBy: ULong,
	var usedBy: ULong? = null,

	val createdAt: LocalDateTime,
	var usedAt: LocalDateTime? = null,

	var used: Boolean = false,
) {
	suspend fun save() =
		Codes.upsert(this)

	fun toStatement(statement: UpdateBuilder<*>) {
		with(CodeTable) {
			statement[id] = this@CodeEntity.id
			statement[ownedBy] = this@CodeEntity.ownedBy
			statement[usedBy] = this@CodeEntity.usedBy
			statement[createdAt] = this@CodeEntity.createdAt
			statement[usedAt] = this@CodeEntity.usedAt
			statement[used] = this@CodeEntity.used
		}
	}

	companion object {
		fun fromRow(row: ResultRow): CodeEntity =
			with(CodeTable) {
				CodeEntity(
					id = row[id].value,
					ownedBy = row[ownedBy],
					usedBy = row[usedBy],
					createdAt = row[createdAt],
					usedAt = row[usedAt],
					used = row[used],
				)
			}
	}
}
