/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites

import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.create.UserMessageCreateBuilder
import dev.kordex.core.extensions.Extension

private lateinit var loggingChannel: TextChannel

suspend fun Extension.getLoggingChannel(): TextChannel {
	if (!::loggingChannel.isInitialized) {
		loggingChannel = bot.kordRef.getChannelOf<TextChannel>(LOGGING_CHANNEL_ID)
			?: error("Unable to find logging channel with ID: $LOGGING_CHANNEL_ID")
	}

	return loggingChannel
}

suspend inline fun Extension.logMessage(crossinline builder: UserMessageCreateBuilder.() -> Unit) =
	getLoggingChannel().createMessage { builder() }
