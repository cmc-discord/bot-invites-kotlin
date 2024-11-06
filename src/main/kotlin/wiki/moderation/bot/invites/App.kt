/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package wiki.moderation.bot.invites

import dev.kord.common.entity.Snowflake
import dev.kordex.core.ExtensibleBot
import dev.kordex.core.utils.env
import dev.kordex.core.utils.envOfOrNull
import dev.kordex.core.utils.envOrNull

val GUILD_ID = envOfOrNull<Snowflake>("GUILD_ID")
	?: Snowflake("1131360407727128576")

private val TOKEN = env("TOKEN")   // Get the bot' token from the env vars or a .env file

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
