/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.extensions.invite

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.optionalString
import dev.kordex.core.commands.converters.impl.string
import wiki.moderation.bot.invites.Translations
import java.util.UUID

fun Arguments.inviteCode() = string {
	name = Translations.Arguments.InviteCode.name
	description = Translations.Arguments.InviteCode.name

	validate {
		try {
			UUID.fromString(value)
		} catch (_: IllegalArgumentException) {
			fail(
				Translations.Errors.invalid_invite_code
					.withNamedPlaceholders("code" to value)
			)
		}
	}
}

class InviteNoteArguments : Arguments() {
	val code by inviteCode()

	val note by optionalString {
		name = Translations.Arguments.Note.name
		description = Translations.Arguments.Note.name
	}
}

class InviteRequestArguments : Arguments() {
	val note by optionalString {
		name = Translations.Arguments.Note.name
		description = Translations.Arguments.Note.name
	}
}

class InviteRevokeArguments : Arguments() {
	val code by inviteCode()
}
