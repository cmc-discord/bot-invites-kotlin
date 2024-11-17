/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

@file:Suppress("MatchingDeclarationName")

package wiki.moderation.bot.invites.extensions.invite

import dev.kordex.core.components.forms.ModalForm
import dev.kordex.core.i18n.types.Key
import wiki.moderation.bot.invites.Translations
import wiki.moderation.bot.invites.extensions.BUTTON_APPLY

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
