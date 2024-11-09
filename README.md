# Discord Bot: Invites

Discord bot managing the invitation system for the CMC.

# Development

## Requirements

You'll need the following to work on this project:

- A local PostgreSQL instance with a testing database (e.g. via [Podman](https://podman-desktop.io/))
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community Edition is fine)
- A JDK, version 17 or later (e.g. [Temurin](https://adoptium.net/temurin/releases/))

## Initial Setup

1. Open the project in IDEA and allow it to refresh the Gradle module.
2. Copy `.env.example` to `.env` and fill it out.
3. Start the "Test" run configuration to make sure your setup works.

## Run Configurations

You can find some run configurations in the project files, which IDEA should find automatically:

- **Apply Licences:** Update the licence headers in all code files.
- **Build:** Lint and build the project. Run this before pushing!
- **Generate Dockerfile:** When you update the configuration in `build.gradle.kts`, use this to update the `Dockerfile`.
- **Get Schema:** Generate table-creation SQL to aid in writing migrations.
- **Test:** Start the bot in development mode for testing.
- **Update Translations:** When you add or remove translation keys in
  `src/main/resources/translations/cmc/strings.properties`, use this to update the generated `Translations` object.

## Internationalisation

This project uses the Kord Extensions i18n framework, which you can learn about
[here](https://docs.kordex.dev/internationalization.html).

**All user-facing strings should be translated, regardless of their content.**

## Database Migrations

This project uses [JetBrains Exposed](https://github.com/JetBrains/Exposed) and [Flyway](https://flywaydb.org/).

You can find the migration scripts in `src/main/resources/db/migration`, written using the PostgreSQL dialect.
These files start with a version number (`V1`), followed by two underscores (`__`) and an underscore-spaced
description — for example, `V1__Create_tables.sql`.

If you create a new table, it is easier to write a migration by updating the `main` function in `db.Database` to
generate table creation SQL, and then running the **Get Schema** run configuration.
This will generate some SQL you can copy into your migration file and modify if needed.
