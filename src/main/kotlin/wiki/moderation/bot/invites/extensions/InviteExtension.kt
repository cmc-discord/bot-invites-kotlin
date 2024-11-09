/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.extensions

import dev.kordex.core.extensions.Extension

class InviteExtension : Extension() {
	override val name: String = "cmc-invites"

	override suspend fun setup() {
		/** TODO: Implementation Details
		 * OK, let's think about this implementation.
		 *
		 * - User joins the server, maybe chats in the public channels for a whiile
		 * - User obtains an invite code from someone with the Verified role (1197550474598023219)
		 * - User clicks on an "Apply" button and is presented with a number of options they can use to record their
		 *   application details (OK, that needs to be in the DB)
		 *   - Require DMs to be open in this process? That way the bot can DM them with an ID they can use to continue
		 *     later
		 * - Once the application is fully submitted, details are sent to a forum channel where we can discuss and
		 *   decide whether to approve them, or deny, optionally with a message explaining why
		 *   - Submitted applications can't be updated but a user can use a new code to apply again some other time.
		 * - Successful applications result in the role being applied
		 */
	}
}
