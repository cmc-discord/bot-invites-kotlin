/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.extensions.invite

import dev.kordex.core.i18n.toKey
import wiki.moderation.bot.invites.db.types.QuestionCategory

val questions = mutableMapOf(
	QuestionCategory.BASIC to mutableMapOf(
		"".toKey() to ""
	),

	QuestionCategory.CMC to mutableMapOf(
		"".toKey() to ""
	),

	QuestionCategory.EXPERIENCE to mutableMapOf(
		"".toKey() to ""
	),

	QuestionCategory.FREEFORM to mutableMapOf(
		"".toKey() to ""
	),
)
