/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.db.types

import dev.kordex.core.i18n.types.Key
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import wiki.moderation.bot.invites.Translations

@Serializable
enum class ApplicationState(val key: Key) {
	@SerialName("accepted")
	ACCEPTED(Translations.Applications.State.accepted),

	@SerialName("denied")
	DENIED(Translations.Applications.State.denied),

	@SerialName("open")
	OPEN(Translations.Applications.State.open),

	@SerialName("submitted")
	SUBMITTED(Translations.Applications.State.submitted),
}
