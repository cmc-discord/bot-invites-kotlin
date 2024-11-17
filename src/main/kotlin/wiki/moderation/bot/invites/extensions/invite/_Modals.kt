/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

@file:Suppress("MatchingDeclarationName")

package wiki.moderation.bot.invites.extensions.invite

import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.components.forms.widgets.ParagraphTextWidget
import dev.kordex.core.i18n.toKey
import dev.kordex.core.i18n.types.Key
import wiki.moderation.bot.invites.Translations
import wiki.moderation.bot.invites.extensions.BUTTON_APPLY

// NOTE: All modal inputs need to have an explicitly set ID.
//       This means that recreating the object won't break
//       field parsing, important in some situations!

class CodeModal : ModalForm() {
	override var title: Key =
		Translations.Modals.Code.title

	init {
		id = BUTTON_APPLY
	}

	val code = lineText {
		id = "code"

		label = Translations.Modals.Code.title
		placeholder = Translations.Modals.Code.Input.placeholder

		required = true
	}
}

class QuestionModal(
	override var title: Key,
	modalId: String,

	val questions: List<QuestionContainer>,
) : ModalForm() {
	val allQuestions: MutableList<ParagraphTextWidget> = mutableListOf()

	init {
		id = modalId

		questions.forEachIndexed { index, container ->
			allQuestions += paragraphText {
				id = "question_$index"

				minLength = 0
				maxLength = 1000
				required = false

				label = container.title
				placeholder = container.placeholder

				initialValue = container.value?.toKey()
			}
		}
	}
}
