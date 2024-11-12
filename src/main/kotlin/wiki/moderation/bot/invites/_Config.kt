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

val STAFF_ROLE_ID = envOfOrNull<Snowflake>("STAFF_ROLE_ID")
	?: Snowflake("1131547978331590808")

val VERIFIED_ROLE_ID = envOfOrNull<Snowflake>("VERIFIED_ROLE_ID")
	?: Snowflake("1197550474598023219")

val LOGGING_CHANNEL_ID = envOfOrNull<Snowflake>("LOGGING_CHANNEL_ID")
	?: Snowflake("1218533825282576424")

val INFO_CHANNEL_ID = envOfOrNull<Snowflake>("INFO_CHANNEL_ID")
	?: Snowflake("1305111270697074728")

val APPLICATION_FORUM_ID = envOfOrNull<Snowflake>("APPLICATION_FORUM_ID")
	?: Snowflake("1305111415903879248")

val ACCEPTED_TAG_ID = envOfOrNull<Snowflake>("ACCEPTED_TAG_ID")
	?: Snowflake("1305111732552859688")

val DENIED_TAG_ID = envOfOrNull<Snowflake>("DENIED_TAG_ID")
	?: Snowflake("1305111776274284545")

val DB_DRIVER: String = envOrNull("DB_DRIVER") ?: "org.postgresql.Driver"

val DB_URL: String = env("DB_URL")
val DB_USER: String = env("DB_USER")
val DB_PASSWORD: String = env("DB_PASSWORD")
