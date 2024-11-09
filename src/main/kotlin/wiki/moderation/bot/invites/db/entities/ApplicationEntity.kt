/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.entities

import dev.kordex.data.api.serializers.KXUUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import wiki.moderation.bot.invites.db.collections.Applications
import wiki.moderation.bot.invites.db.tables.ApplicationTable
import wiki.moderation.bot.invites.db.types.ApplicationState
import wiki.moderation.bot.invites.db.types.QuestionCategory
import java.util.UUID

@Serializable
@Suppress("DataClassContainsFunctions", "DataClassShouldBeImmutable")
data class ApplicationEntity(
	@Serializable(with = KXUUIDSerializer::class)
	val id: UUID = UUID.randomUUID(),
	val applicant: ULong,

	@Serializable(with = KXUUIDSerializer::class)
	var code: UUID? = null,
	var state: ApplicationState = ApplicationState.OPEN,
	val questions: MutableMap<QuestionCategory, MutableMap<String, String>> = mutableMapOf(),
	val threadId: ULong? = null,
) {
	suspend fun save() =
		Applications.upsert(this)

	fun toStatement(statement: UpdateBuilder<*>) {
		with(ApplicationTable) {
			statement[id] = this@ApplicationEntity.id
			statement[applicant] = this@ApplicationEntity.applicant
			statement[code] = this@ApplicationEntity.code
			statement[state] = this@ApplicationEntity.state
			statement[questions] = this@ApplicationEntity.questions
			statement[threadId] = this@ApplicationEntity.threadId
		}
	}

	companion object {
		fun fromRow(row: ResultRow): ApplicationEntity =
			with(ApplicationTable) {
				ApplicationEntity(
					id = row[id].value,
					applicant = row[applicant],
					code = row[code],
					state = row[state],
					questions = row[questions],
					threadId = row[threadId],
				)
			}
	}
}
