/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites

import com.ibm.icu.text.MessageFormat
import dev.kord.common.entity.Snowflake
import java.util.UUID

object Resources {
	fun get(path: String) =
		Resources::class.java.getResource(path)

	object Text {
		fun getVerifiedChannelText() =
			Resources.get("text/get-verified.md")!!.readText()

		fun getApplicationMessage(
			application: UUID,
			infoChannel: Snowflake,
			sets: Int,
			questions: Int
		): String {
			val text = Resources.get("text/application-message.md")!!.readText()

			val format = MessageFormat(text)

			return format.format(
				mapOf(
					"application" to application,
					"info_channel" to infoChannel,
					"sets" to sets,
					"questions" to questions
				)
			)
		}
	}
}
