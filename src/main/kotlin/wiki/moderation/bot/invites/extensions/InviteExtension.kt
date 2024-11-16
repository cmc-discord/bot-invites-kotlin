/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package wiki.moderation.bot.invites.extensions

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.builder.components.emoji
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed
import dev.kordex.core.DiscordRelayedException
import dev.kordex.core.checks.hasRole
import dev.kordex.core.commands.application.slash.ephemeralSubCommand
import dev.kordex.core.events.ModalInteractionCompleteEvent
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.ephemeralSlashCommand
import dev.kordex.core.extensions.event
import dev.kordex.core.i18n.EMPTY_KEY
import dev.kordex.core.i18n.withContext
import wiki.moderation.bot.invites.*
import wiki.moderation.bot.invites.checks.componentIdIs
import wiki.moderation.bot.invites.checks.componentIdStartsWith
import wiki.moderation.bot.invites.checks.modalIdIs
import wiki.moderation.bot.invites.checks.modalIdStartsWith
import wiki.moderation.bot.invites.db.collections.Codes
import wiki.moderation.bot.invites.db.collections.Users
import wiki.moderation.bot.invites.extensions.invite.InviteNoteArguments
import wiki.moderation.bot.invites.extensions.invite.InviteRequestArguments
import wiki.moderation.bot.invites.extensions.invite.InviteRevokeArguments
import java.util.*

const val BUTTON_APPLY = "invites/start-application"
const val BUTTON_QUESTION_PREFIX = "invites/questions/"

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

		// admin
		ephemeralSlashCommand {
			name = Translations.Commands.Admin.name
			description = Translations.Commands.Admin.description

			guild(GUILD_ID)

			check { hasRole(STAFF_ROLE_ID) }

			// TODO: Invite management commands

			// get-commands
			ephemeralSubCommand {
				name = Translations.Commands.Admin.GetCommands.name
				description = Translations.Commands.Admin.GetCommands.description

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

			// get-settings
			ephemeralSubCommand {
				name = Translations.Commands.Admin.GetSettings.name
				description = Translations.Commands.Admin.GetSettings.description

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

			// refresh-info-channel
			ephemeralSubCommand {
				name = Translations.Commands.Admin.Refresh.name
				description = Translations.Commands.Admin.Refresh.description

				action {
					val channel = this@ephemeralSubCommand.kord.getChannelOf<TextChannel>(INFO_CHANNEL_ID)

					if (channel == null) {
						throw DiscordRelayedException(
							Translations.Errors.info_channel_missing
								.withContext(this@action)
								.withNamedPlaceholders("id" to INFO_CHANNEL_ID)
						)
					}

					channel.messages.collect { message -> message.delete() }

					Resources.Text.getVerifiedChannelText()
						.forEach { block -> channel.createMessage(block) }

					channel.createMessage {
						actionRow {
							interactionButton(ButtonStyle.Primary, BUTTON_APPLY) {
								label = Translations.Terms.apply
									.withContext(this@action)
									.translate()

								emoji(ReactionEmoji.Unicode("✍️"))
							}
						}
					}

					respond {
						content = Translations.Responses.channel_refreshed
							.withContext(this@action)
							.translateNamed("id" to INFO_CHANNEL_ID)
					}
				}
			}
		}

		// invites
		ephemeralSlashCommand {
			name = Translations.Commands.Invites.name
			description = Translations.Commands.Invites.description

			// note
			ephemeralSubCommand(::InviteNoteArguments) {
				name = Translations.Commands.Invites.Note.name
				description = Translations.Commands.Invites.Note.description

				action {
					val uuid = UUID.fromString(arguments.code)

					val code = Codes.readOwned(uuid, user.id)
						?: throw DiscordRelayedException(
							Translations.Errors.invalid_invite_code
								.withContext(this@action)
								.withNamedPlaceholders("code" to arguments.code)
						)

					if (arguments.note != null) {
						code.note = arguments.note

						Codes.upsert(code)

						logMessage {
							title = Translations.Logging.Invites.NoteSet.title
								.translate()

							description = Translations.Logging.Invites.NoteSet.description
								.translateNamed(
									"note" to arguments.note
								)

							codeField(code.id)
							userField(user)
						}

						respond {
							content = Translations.Responses.code_note_set
								.withContext(this@action)
								.translateNamed(
									"code" to arguments.code,
									"note" to arguments.note
								)
						}
					} else {
						if (code.note == null) {
							throw DiscordRelayedException(
								Translations.Errors.code_missing_note
									.withContext(this@action)
									.withNamedPlaceholders("code" to arguments.code)
							)
						} else {
							respond {
								content = Translations.Responses.code_note
									.withContext(this@action)
									.translateNamed(
										"code" to arguments.code,
										"note" to code.note
									)
							}
						}
					}
				}
			}

			// request
			ephemeralSubCommand(::InviteRequestArguments) {
				name = Translations.Commands.Invites.Request.name
				description = Translations.Commands.Invites.Request.description

				action {
					val userEntity = Users.getOrCreate(user)

					if (userEntity.codesRemaining < 1) {
						logMessage {
							title = Translations.Logging.Invites.Request.Denied.title
								.translate()

							description = Translations.Logging.Invites.Request.Denied.description
								.translate()

							userField(user)
						}

						throw DiscordRelayedException(
							Translations.Errors.no_remaining_codes
								.withContext(this@action)
						)
					}

					userEntity.codesRemaining -= 1

					Users.update(userEntity)

					val code = Codes.create(userEntity, arguments.note)

					logMessage {
						title = Translations.Logging.Invites.Request.Granted.title
							.translate()

						description = Translations.Logging.Invites.Request.Granted.description
							.translate()

						codeField(code.id)
						userField(user)
					}

					respond {
						content = Translations.Responses.code_created
							.withContext(this@action)
							.translateNamed("code" to code.id)
					}
				}
			}

			// revoke
			ephemeralSubCommand(::InviteRevokeArguments) {
				name = Translations.Commands.Invites.Revoke.name
				description = Translations.Commands.Invites.Revoke.description

				action {
					val uuid = UUID.fromString(arguments.code)
					val code = Codes.readOwned(uuid, user.id)

					if (code == null) {
						logMessage {
							title = Translations.Logging.Invites.Revoke.Denied.title
								.translate()

							description = Translations.Logging.Invites.Revoke.Denied.description
								.translate()

							codeField(uuid)
							userField(user)
						}

						throw DiscordRelayedException(
							Translations.Errors.invalid_invite_code
								.withContext(this@action)
								.withNamedPlaceholders("code" to arguments.code)
						)
					}

					Codes.delete(code.id)

					logMessage {
						title = Translations.Logging.Invites.Revoke.Granted.title
							.translate()

						description = Translations.Logging.Invites.Revoke.Granted.description
							.translate()

						codeField(uuid)
						userField(user)
					}

					respond {
						content = Translations.Responses.code_revoked
							.withContext(this@action)
							.translateNamed("code" to code.id)
					}
				}
			}

			// show-unused
			ephemeralSubCommand {
				name = Translations.Commands.Invites.ShowUnused.name
				description = Translations.Commands.Invites.ShowUnused.description

				action {
					val codes = Codes.getForOwner(user.id) { !it.used }

					if (codes.isEmpty()) {
						throw DiscordRelayedException(
							Translations.Errors.no_unused_codes
								.withContext(this@action)
						)
					}

					editingPaginator(EMPTY_KEY, getLocale()) {
						codes.chunked(10).map { chunk ->
							page {
								title = Translations.Responses.UnusedCodes.title
									.withContext(this@action)
									.translate()

								val lines: MutableList<String> = mutableListOf()

								for (code in chunk) {
									lines.add(
										Translations.Responses.UnusedCodes.line
											.withContext(this@action)
											.translateNamed(
												"code" to code.id,
												"owner" to code.ownedBy
											)
									)
								}

								description = lines.joinToString("\n")
							}
						}
					}.send()
				}
			}

			// show-used
			ephemeralSubCommand {
				name = Translations.Commands.Invites.ShowUsed.name
				description = Translations.Commands.Invites.ShowUsed.description

				action {
					val codes = Codes.getForOwner(user.id) { it.used }

					if (codes.isEmpty()) {
						throw DiscordRelayedException(
							Translations.Errors.no_used_codes
								.withContext(this@action)
						)
					}

					editingPaginator(EMPTY_KEY, getLocale()) {
						codes.chunked(10).map { chunk ->
							page {
								title = Translations.Responses.UsedCodes.title
									.withContext(this@action)
									.translate()

								val lines: MutableList<String> = mutableListOf()

								for (code in chunk) {
									lines.add(
										Translations.Responses.UsedCodes.line
											.withContext(this@action)
											.translateNamed(
												"code" to code.id,
												"owner" to code.ownedBy
											)
									)
								}

								description = lines.joinToString("\n")
							}
						}
					}.send()
				}
			}

			// status
			ephemeralSubCommand {
				name = Translations.Commands.Invites.Status.name
				description = Translations.Commands.Invites.Status.description

				action {
					val codes = Codes.getForOwner(user.id)
					val userEntity = Users.getOrCreate(user)
					val partitioned = codes.partition { !it.used }  // Pair<Unused, Used>

					respond {
						embed {
							title = Translations.Responses.User.Status.title
								.withContext(this@action)
								.translate()

							description = Translations.Responses.User.Status.description
								.withContext(this@action)
								.translateNamed(
									"remaining" to userEntity.codesRemaining,
									"unused" to partitioned.first.count(),
									"used" to partitioned.second.count(),
								)
						}
					}
				}
			}
		}

		event<ButtonInteractionCreateEvent> {
			check { componentIdIs(BUTTON_APPLY) }

			action {
				// TODO: Attempt to DM, error if failure
				// TODO: Open Modal for invite code
			}
		}

		event<ButtonInteractionCreateEvent> {
			check { componentIdStartsWith(BUTTON_QUESTION_PREFIX) }

			action {
				// TODO: Get question type from component ID
				// TODO: Retrieve existing answers from DB if any
				// TODO: Respond with Modal containing that info
			}
		}

		event<ModalInteractionCompleteEvent> {
			check { modalIdIs(BUTTON_APPLY) }

			action {
				// TODO: Check invite code exists and mark it as used
				// TODO: If code invalid, error
				// TODO: Create application entry in DB
				// TODO: DM user with instructions
			}
		}

		event<ModalInteractionCompleteEvent> {
			check { modalIdStartsWith(BUTTON_QUESTION_PREFIX) }

			action {
				// TODO: Check question type from ID
				// TODO: Retrieve existing DB data
				// TODO: Overwrite provided questions in DB
			}
		}
	}
}
