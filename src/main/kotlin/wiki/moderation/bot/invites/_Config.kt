/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites

import dev.kord.common.entity.Snowflake
import dev.kordex.core.utils.env
import dev.kordex.core.utils.envOfOrNull
import dev.kordex.core.utils.envOrNull

val GUILD_ID = envOfOrNull<Snowflake>("GUILD_ID")
	?: Snowflake("1131360407727128576")

val DB_DRIVER: String = envOrNull("DB_DRIVER") ?: "org.postgresql.Driver"

val DB_URL: String = env("DB_URL")
val DB_USER: String = env("DB_USER")
val DB_PASSWORD: String = env("DB_PASSWORD")
