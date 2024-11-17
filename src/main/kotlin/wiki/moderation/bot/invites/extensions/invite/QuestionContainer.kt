/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.extensions.invite

import dev.kordex.core.i18n.types.Key

data class QuestionContainer(
	val title: Key,
	val placeholder: Key,
	val value: String? = null,
)
