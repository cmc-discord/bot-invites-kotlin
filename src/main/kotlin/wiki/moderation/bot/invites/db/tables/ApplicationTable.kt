/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.tables

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.json.json
import wiki.moderation.bot.invites.db.types.ApplicationState
import wiki.moderation.bot.invites.db.types.QuestionCategory

@Suppress("MagicNumber")
object ApplicationTable : UUIDTable("application") {
	val applicant = ulong("applicant")
	val code = uuid("code").nullable()
	val state = enumerationByName<ApplicationState>("state", 20).default(ApplicationState.OPEN)
	val threadId = ulong("thread_id").nullable()

	val questions = json<MutableMap<QuestionCategory, MutableMap<String, String>>>("questions", Json)
		.default(mutableMapOf())
}
