package dev.mieser.tsa.web

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.Test

internal class ApplicationVersionServiceTest {

    @Test
    fun applicationVersionStripsLeadingAndTrailingWhitespace() {
        // given
        val testSubject = ApplicationVersionService("version-with-whitespace.txt")

        // when
        val applicationVersion = testSubject.applicationVersion

        // then
        assertThat(applicationVersion).isEqualTo("1.2.3")
    }

    @Test
    fun applicationVersionReadsFileWithExpectedEncoding() {
        // given
        val testSubject = ApplicationVersionService("version-with-umlaut.txt")

        // when
        val applicationVersion = testSubject.applicationVersion

        // then
        assertThat(applicationVersion).isEqualTo("1.ü")
    }

    @Test
    fun applicationVersionThrowsExceptionWhenVersionFileWasNotFound() {
        // given
        val testSubject = ApplicationVersionService("unknown-file.txt")

        // when / then
        assertThatIllegalStateException()
                .isThrownBy { testSubject.applicationVersion }
                .withMessage("Application version file was not found on the classpath.")
    }

    @Test
    fun applicationVersionReadsGeneratedVersionFileByDefault() {
        // given
        val testSubject = ApplicationVersionService()

        // when
        val applicationVersion = testSubject.applicationVersion

        // then
        assertThat(applicationVersion).isNotBlank
    }

}
