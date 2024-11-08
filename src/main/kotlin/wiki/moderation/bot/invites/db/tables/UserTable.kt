/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.tables

import org.jetbrains.exposed.dao.id.ULongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UserTable : ULongIdTable("user") {
	val inviteCode = uuid("invite_code").nullable().index("user_invite_code")

	val lastJoined = datetime("last_joined")
	val lastSeen = datetime("last_seen")

	val codesRemaining = integer("codes_remaining").default(0)
}
