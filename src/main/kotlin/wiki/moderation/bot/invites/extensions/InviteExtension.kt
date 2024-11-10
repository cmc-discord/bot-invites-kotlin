/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.extensions

import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.message.embed
import dev.kordex.core.checks.hasRole
import dev.kordex.core.events.ModalInteractionCompleteEvent
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.ephemeralSlashCommand
import dev.kordex.core.extensions.event
import dev.kordex.core.i18n.withContext
import wiki.moderation.bot.invites.*
import wiki.moderation.bot.invites.checks.componentIdStartsWith

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

		ephemeralSlashCommand {
			name = Translations.Commands.GetCommands.name
			description = Translations.Commands.GetCommands.description

			guild(GUILD_ID)

			check { hasRole(STAFF_ROLE_ID) }

			action {
				respond {
					embed {
						title = Translations.Embeds.GetCommands.title
							.withContext(this@action)
							.translate()

						description = buildString {
							slashCommands.sortedBy { it.name.key }.forEach { command ->
								appendLine("${command.mention} -> `${command.mention}`")
							}
						}
					}
				}
			}
		}

		ephemeralSlashCommand {
			name = Translations.Commands.GetSettings.name
			description = Translations.Commands.GetSettings.description

			guild(GUILD_ID)

			check { hasRole(STAFF_ROLE_ID) }

			action {
				respond {
					embed {
						title = Translations.Embeds.GetSettings.title
							.withContext(this@action)
							.translate()

						description = Translations.Embeds.GetSettings.description
							.withContext(this@action)
							.translateNamed(
								"server" to GUILD_ID,
								"forum_channel" to APPLICATION_FORUM_ID,
								"info_channel" to INFO_CHANNEL_ID,
								"staff_role" to STAFF_ROLE_ID,
								"verified_role" to VERIFIED_ROLE_ID,
								"accepted_tag" to ACCEPTED_TAG_ID,
								"denied_tag" to DENIED_TAG_ID,
							)
					}
				}
			}
		}

		ephemeralSlashCommand {
			name = Translations.Commands.Refresh.name
			description = Translations.Commands.Refresh.description

			guild(GUILD_ID)

			check { hasRole(STAFF_ROLE_ID) }

			action { }
		}

		event<ButtonInteractionCreateEvent> {
			check { componentIdStartsWith("invites/") }

			action { }
		}

		event<ModalInteractionCompleteEvent> {
			action { }
		}
	}
}
