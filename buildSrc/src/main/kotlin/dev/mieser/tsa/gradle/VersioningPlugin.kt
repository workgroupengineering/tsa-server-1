package dev.mieser.tsa.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.process.ExecOperations
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import javax.inject.Inject

/**
 * Sets the project version based on the [GITHUB_REF](https://docs.github.com/en/actions/learn-github-actions/variables#default-environment-variables)
 * environment variable or the current branch name, when the environment variable is not set. When the `projectVersion` project property is set, its
 * value is used as the project version.
 */
class VersioningPlugin @Inject constructor(private val execOperations: ExecOperations) : Plugin<Project> {

    private val log = LoggerFactory.getLogger(VersioningPlugin::class.java)

    private companion object {

        const val PROJECT_VERSION_PROPERTY_NAME = "projectVersion"

        const val GITHUB_REF = "GITHUB_REF"

        const val GITHUB_REF_FORMAT = "^refs/(?<type>heads|pull|tags)/(?<value>.+)$"

        const val PULL_REQUEST_PREFIX = "PR"

        const val SNAPSHOT_SUFFIX = "SNAPSHOT"

    }

    override fun apply(project: Project) {
        determineVersion(project).run {
            log.info("Setting project version to '{}'.", this)
            project.version = this
        }

        project.tasks.register("printVersion") {
            description = "Print the Project Version to stdout"
            doLast {
                println(project.version)
            }
        }
    }

    private fun determineVersion(project: Project): String {
        if (project.hasProperty(PROJECT_VERSION_PROPERTY_NAME)) {
            log.info("Using the value of the '{}' property as the project version.", PROJECT_VERSION_PROPERTY_NAME)
            return project.property(PROJECT_VERSION_PROPERTY_NAME) as String
        }

        return readRefFromEnvironment()?.run(::formatRef) ?: determineVersionFromCurrentEnv()
    }

    private fun determineVersionFromCurrentEnv(): String {
        log.info("Inferring project version from current git branch name...")

        val standardOut = ByteArrayOutputStream()
        try {
            execOperations.exec {
                commandLine("git", "symbolic-ref", "--short", "HEAD")
                standardOutput = standardOut
            }
        } catch (e: Exception) {
            log.info("Failed to execute git command! Is git installed?", e)
            return Project.DEFAULT_VERSION
        }

        val currentBranch = standardOut.toString(UTF_8).trim()
        log.info("Current branch: {}", currentBranch)
        return "${currentBranch.substringAfterLast('/')}-$SNAPSHOT_SUFFIX"
    }

    private fun readRefFromEnvironment(): Ref? {
        val ref = System.getenv()[GITHUB_REF]
        if (ref.isNullOrBlank()) {
            log.info("Environment variable '{}' is not set.", GITHUB_REF)
            return null
        }

        val matcher = GITHUB_REF_FORMAT.toPattern().matcher(ref)
        if (!matcher.matches()) {
            log.warn(
                "The value of environment variable '{}' ('{}') does not match the expected format!",
                GITHUB_REF,
                ref
            )
            return null
        }

        val type = Type.values().find { it.identifier == matcher.group("type") }!!
        val value = matcher.group("value")

        return Ref(type, value)
    }

    private fun formatRef(ref: Ref): String {
        return when (ref.type) {
            Type.TAG -> ref.value

            Type.PULL_REQUEST -> {
                val pullRequestNumber = ref.value.substringBefore('/')
                "$PULL_REQUEST_PREFIX-$pullRequestNumber-$SNAPSHOT_SUFFIX"
            }

            Type.BRANCH -> "${ref.value.replace('/', '-')}-$SNAPSHOT_SUFFIX"
        }
    }

    private data class Ref(
        val type: Type,
        val value: String
    )

    private enum class Type(val identifier: String) {

        BRANCH("heads"),

        PULL_REQUEST("pull"),

        TAG("tags")

    }

}