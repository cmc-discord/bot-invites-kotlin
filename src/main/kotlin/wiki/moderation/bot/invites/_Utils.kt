/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites

import dev.kord.core.behavior.UserBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed
import dev.kordex.core.extensions.Extension
import java.util.Locale
import java.util.UUID

private lateinit var loggingChannel: TextChannel

suspend fun Extension.getLoggingChannel(): TextChannel {
	if (!::loggingChannel.isInitialized) {
		loggingChannel = bot.kordRef.getChannelOf<TextChannel>(LOGGING_CHANNEL_ID)
			?: error("Unable to find logging channel with ID: $LOGGING_CHANNEL_ID")
	}

	return loggingChannel
}

suspend inline fun Extension.logMessage(builder: suspend EmbedBuilder.() -> Unit) =
	getLoggingChannel().createMessage {
		embed { builder() }
	}

fun EmbedBuilder.codeField(code: UUID, locale: Locale? = null, inline: Boolean = true) {
	field {
		this.inline = inline

		name = Translations.Terms.code
			.withLocale(locale)
			.translate()

		value = "`$code`"
	}
}

suspend fun EmbedBuilder.userField(user: UserBehavior, locale: Locale? = null, inline: Boolean = true) {
	val userObj = user.asUserOrNull()

	field {
		this.inline = inline

		name = Translations.Terms.user
			.withLocale(locale)
			.translate()

		value = if (userObj != null) {
			"${userObj.mention} (`${userObj.tag}` / `${userObj.id}`)"
		} else {
			"${user.mention} (`${user.id}`)"
		}
	}
}
