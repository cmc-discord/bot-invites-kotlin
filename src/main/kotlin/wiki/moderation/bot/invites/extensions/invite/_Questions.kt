/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.extensions.invite

import dev.kordex.core.i18n.types.Key
import wiki.moderation.bot.invites.Translations
import wiki.moderation.bot.invites.db.types.QuestionCategory

/** Questions keyed by their category, inner map is the title key to a placeholder key. **/
val questions: Map<QuestionCategory, Map<Key, Key>> = mapOf(
	QuestionCategory.BASIC to mapOf(
		Translations.Applications.Question.Basic1.title to
			Translations.Applications.Question.Basic1.placeholder,

		Translations.Applications.Question.Basic2.title to
			Translations.Applications.Question.Basic2.placeholder,

		Translations.Applications.Question.Basic3.title to
			Translations.Applications.Question.Basic3.placeholder,
	),

	QuestionCategory.CMC to mapOf(
		Translations.Applications.Question.Cmc1.title to
			Translations.Applications.Question.Cmc1.placeholder,

		Translations.Applications.Question.Cmc2.title to
			Translations.Applications.Question.Cmc2.placeholder,

		Translations.Applications.Question.Cmc3.title to
			Translations.Applications.Question.Cmc3.placeholder,
	),

	QuestionCategory.EXPERIENCE to mapOf(
		Translations.Applications.Question.Experience1.title to
			Translations.Applications.Question.Experience1.placeholder,

		Translations.Applications.Question.Experience2.title to
			Translations.Applications.Question.Experience2.placeholder,

		Translations.Applications.Question.Experience3.title to
			Translations.Applications.Question.Experience3.placeholder,
	),

	QuestionCategory.FREEFORM to mapOf(
		Translations.Applications.Question.Freeform1.title to
			Translations.Applications.Question.Freeform1.placeholder,

		Translations.Applications.Question.Freeform2.title to
			Translations.Applications.Question.Freeform2.placeholder,

		Translations.Applications.Question.Freeform3.title to
			Translations.Applications.Question.Freeform3.placeholder,
	),
)
