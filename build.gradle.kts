import dev.kordex.gradle.plugins.docker.file.*
import dev.kordex.gradle.plugins.kordex.DataCollection

plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")

	id("com.github.johnrengelman.shadow")
	id("io.gitlab.arturbosch.detekt")

	id("dev.kordex.gradle.docker")
	id("dev.kordex.gradle.kordex")

	id("dev.yumi.gradle.licenser")
	id("io.sentry.jvm.gradle")
}

group = "wiki.moderation"
version = "1.0-SNAPSHOT"

dependencies {
	detektPlugins(libs.detekt)

	implementation(libs.kotlin.stdlib)
	implementation(libs.kx.ser)

	// Logging dependencies
	implementation(libs.groovy)
	implementation(libs.jansi)
	implementation(libs.logback)
	implementation(libs.logback.groovy)
	implementation(libs.logging)
}

kordEx {
	kordExVersion = "2.3.1-SNAPSHOT"

	bot {
		// See https://docs.kordex.dev/data-collection.html
		dataCollection(DataCollection.Standard)

		mainClass = "wiki.moderation.bot.invites.AppKt"
	}

	i18n {
		classPackage = "wiki.moderation.bot.invites"
		translationBundle = "cmc.strings"
	}
}

detekt {
	buildUponDefaultConfig = true

	config.from(rootProject.files("detekt.yml"))
}

license {
	rule(rootProject.file("codeformat/HEADER"))

	exclude("build/**")
}

docker {
	// Create the Dockerfile in the root folder.
	file(rootProject.file("Dockerfile"))

	commands {
		// Each function (aside from comment/emptyLine) corresponds to a Dockerfile instruction.
		// See: https://docs.docker.com/reference/dockerfile/

		from("openjdk:21-jdk-slim")

		emptyLine()

		runShell("mkdir -p /bot/plugins")
		runShell("mkdir -p /bot/data")

		emptyLine()

		copy("build/libs/$name-*-all.jar", "/bot/bot.jar")

		emptyLine()

		// Add volumes for locations that you need to persist. This is important!
		volume("/bot/data")  // Storage for data files
		volume("/bot/plugins")  // Plugin ZIP/JAR location

		emptyLine()

		workdir("/bot")

		emptyLine()

		entryPointExec(
			"java", "-Xms2G", "-Xmx2G",
			"-jar", "/bot/bot.jar"
		)
	}
}

if (System.getenv().containsKey("SENTRY_AUTH_TOKEN")) {
	sentry {
		includeSourceContext = true

		org = "community-management-community"
		projectName = "bot-general"
		authToken = System.getenv("SENTRY_AUTH_TOKEN")
	}
} else {
	logger.info("Not sending sources to Sentry as the 'SENTRY_AUTH_TOKEN' env var isn't set.")
}
