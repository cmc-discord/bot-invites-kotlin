/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package wiki.moderation.bot.invites

import dev.kordex.core.ExtensibleBot
import dev.kordex.core.utils.env
import dev.kordex.core.utils.envOrNull

private val TOKEN = env("TOKEN")

suspend fun main() {
	val bot = ExtensibleBot(TOKEN) {
		applicationCommands {
			defaultGuild(GUILD_ID)
		}

		extensions {
			sentry {
				enableIfDSN(envOrNull("SENTRY_DSN"))
			}
		}
	}

	bot.start()
}
