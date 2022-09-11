package dev.mieser.tsa.web

import org.apache.commons.lang3.mutable.MutableObject
import org.springframework.stereotype.Service

/**
 * Service to retrieve the current version of the application. The application version file is generated in the build
 * process.
 *
 * @param versionFilePath The path the application version file will be read from.
 * @param cachedVersion Caches the read application version.
 */
@Service("applicationVersionService")
open class ApplicationVersionService constructor(private val versionFilePath: String) {

    private val log by Slf4jLoggerFactory()

    private val cachedVersion: MutableObject<String?> = MutableObject(null)

    constructor() : this(DEFAULT_VERSION_FILE_PATH)

    /**
     * @return The current application version.
     */
    open val applicationVersion: String?
        get() {
            if (cachedVersion.value == null) {
                synchronized(cachedVersion) {
                    cachedVersion.value = readVersionFileFromManifest()
                    log.debug("Successfully read application version '{}' from '{}'.", cachedVersion.value, versionFilePath)
                }
            }
            return cachedVersion.value
        }

    /**
     * @return The content of the application version file with leading and trailing whitespace removed.
     * @throws IllegalStateException
     * When the application version file was not found or could not be read.
     */
    private fun readVersionFileFromManifest(): String {
        val versionFile = javaClass.classLoader.getResource(versionFilePath)
                ?: throw IllegalStateException("Application version file was not found on the classpath.")
        return versionFile.readText().trim()
    }

    companion object {

        /**
         * The path the generated application version file is located at.
         */
        private const val DEFAULT_VERSION_FILE_PATH = "META-INF/application-version.txt"

    }

}
