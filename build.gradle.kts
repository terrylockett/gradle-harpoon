import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
	id("java") 
	alias(libs.plugins.kotlin) 
	alias(libs.plugins.intelliJPlatform) 
	alias(libs.plugins.changelog) 
	alias(libs.plugins.kover) 
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin {
	jvmToolchain(17)
}

repositories {
	mavenCentral()

	// IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
	intellijPlatform {
		defaultRepositories()
		localPlatformArtifacts()
	}
}

dependencies {
	testImplementation(libs.junit)

	// IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
	intellijPlatform {
		create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))

		// Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
		bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
		// Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
		plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

		instrumentationTools()
		pluginVerifier()
		zipSigner()
		testFramework(TestFrameworkType.Platform)
	}
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
	pluginConfiguration {
		version = providers.gradleProperty("pluginVersion")

		// Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
		description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
			val start = "<!-- Plugin description -->"
			val end = "<!-- Plugin description end -->"

			with(it.lines()) {
				if (!containsAll(listOf(start, end))) {
					throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
				}
				subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
			}
		}

		ideaVersion {
			sinceBuild = providers.gradleProperty("pluginSinceBuild")
			untilBuild = providers.gradleProperty("pluginUntilBuild")
		}
	}

	signing {
		certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
		privateKey = providers.environmentVariable("PRIVATE_KEY")
		password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
	}

	publishing {
		token = providers.environmentVariable("PUBLISH_TOKEN")
		// The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
		// Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
		// https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
		channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
	}

	pluginVerification {
		ides {
			recommended()
		}
	}
}


// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover {
	reports {
		total {
			xml {
				onCheck = true
			}
		}
	}
}

tasks {
	wrapper {
		gradleVersion = providers.gradleProperty("gradleVersion").get()
	}
}

intellijPlatformTesting {
	runIde {
		register("runIdeForUiTests") {
			task {
				jvmArgumentProviders += CommandLineArgumentProvider {
					listOf(
						"-Drobot-server.port=8082",
						"-Dide.mac.message.dialogs.as.sheets=false",
						"-Djb.privacy.policy.text=<!--999.999-->",
						"-Djb.consents.confirmation.enabled=false",
					)
				}
			}

			plugins {
				robotServerPlugin()
			}
		}
	}
}
