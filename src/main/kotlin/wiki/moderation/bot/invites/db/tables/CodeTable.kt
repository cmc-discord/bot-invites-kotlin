/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CodeTable : UUIDTable("code") {
	val ownedBy = ulong("owned_by").index("code_owned_by")
	val usedBy = ulong("used_by").nullable()

	val createdAt = datetime("created_at")
	val usedAt = datetime("used_at").nullable()

	val used = bool("used").default(false)
	val note = text("note").nullable()
}
