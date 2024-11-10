/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.checks

import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kordex.core.checks.failed
import dev.kordex.core.checks.passed
import dev.kordex.core.checks.types.CheckContext
import io.github.oshai.kotlinlogging.KotlinLogging
import wiki.moderation.bot.invites.Translations

fun <T : ComponentInteractionCreateEvent> CheckContext<T>.componentIdStartsWith(prefix: String) {
	if (!passed) {
		return
	}

	val logger = KotlinLogging.logger("wiki.moderation.bot.invites.checks.componentIdStartsWith")

	if (event.interaction.componentId.startsWith(prefix)) {
		logger.passed("Component ID `${event.interaction.componentId}` starts with `$prefix`")
	} else {
		logger.failed("Component ID `${event.interaction.componentId}` doesn't start with `$prefix`")

		fail(
			Translations.Errors.unknown_component
				.withNamedPlaceholders("id" to event.interaction.componentId)
		)
	}
}
