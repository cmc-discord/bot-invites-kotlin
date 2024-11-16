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
import dev.kordex.core.events.ModalInteractionCompleteEvent
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

fun <T : ComponentInteractionCreateEvent> CheckContext<T>.componentIdIs(id: String) {
	if (!passed) {
		return
	}

	val logger = KotlinLogging.logger("wiki.moderation.bot.invites.checks.componentIdIs")

	if (event.interaction.componentId == id) {
		logger.passed("Component ID `${event.interaction.componentId}` is `$id`")
	} else {
		logger.failed("Component ID `${event.interaction.componentId}` isn't `$id`")

		fail(
			Translations.Errors.unknown_component
				.withNamedPlaceholders("id" to event.interaction.componentId)
		)
	}
}

fun CheckContext<ModalInteractionCompleteEvent>.modalIdStartsWith(prefix: String) {
	if (!passed) {
		return
	}

	val logger = KotlinLogging.logger("wiki.moderation.bot.invites.checks.modalIdStartsWith")

	if (event.interaction.modalId.startsWith(prefix)) {
		logger.passed("Modal ID `${event.interaction.modalId}` starts with `$prefix`")
	} else {
		logger.failed("Modal ID `${event.interaction.modalId}` doesn't start with `$prefix`")

		fail(
			Translations.Errors.unknown_modal
				.withNamedPlaceholders("id" to event.interaction.modalId)
		)
	}
}

fun CheckContext<ModalInteractionCompleteEvent>.modalIdIs(id: String) {
	if (!passed) {
		return
	}

	val logger = KotlinLogging.logger("wiki.moderation.bot.invites.checks.modalIdIs")

	if (event.interaction.modalId == id) {
		logger.passed("Modal ID `${event.interaction.modalId}` is `$id`")
	} else {
		logger.failed("Modal ID `${event.interaction.modalId}` isn't `$id`")

		fail(
			Translations.Errors.unknown_modal
				.withNamedPlaceholders("id" to event.interaction.modalId)
		)
	}
}
