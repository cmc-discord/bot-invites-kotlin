/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.types

import dev.kordex.core.i18n.types.Key
import kotlinx.serialization.Serializable
import wiki.moderation.bot.invites.Translations

@Serializable
enum class QuestionCategory(val nameKey: Key, val descriptionKey: Key) {
	BASIC(
		Translations.Applications.Questions.Basic.name,
		Translations.Applications.Questions.Basic.description,
	),

	CMC(
		Translations.Applications.Questions.Cmc.name,
		Translations.Applications.Questions.Cmc.description,
	),

	EXPERIENCE(
		Translations.Applications.Questions.Experience.name,
		Translations.Applications.Questions.Experience.description,
	),

	FREEFORM(
		Translations.Applications.Questions.Freeform.name,
		Translations.Applications.Questions.Freeform.description,
	),
}
