package dev.mieser.tsa.web.config

import dev.mieser.tsa.web.config.properties.HttpsRedirectProperties
import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.willAnswer
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory

@ExtendWith(MockitoExtension::class)
internal class HttpsRedirectConfigurationTest {

    private val httpsRedirectProperties: HttpsRedirectProperties = HttpsRedirectProperties()

    private val serverProperties: ServerProperties = ServerProperties()

    private val testSubject: HttpsRedirectConfiguration = HttpsRedirectConfiguration(httpsRedirectProperties, serverProperties)

    @Test
    fun customizeAddsConnectorWithDefaultRedirectPortWhenServerPropertyNotSet(@Mock factoryMock: TomcatServletWebServerFactory) {
        // given / when
        testSubject.customize(factoryMock)

        // then
        val connectorCaptor = ArgumentCaptor.forClass(Connector::class.java)

        then(factoryMock).should().addAdditionalTomcatConnectors(connectorCaptor.capture())
        assertThat(connectorCaptor.value.redirectPort).isEqualTo(8080)
    }

    @Test
    fun customizeAddsConnectorWithConfiguredRedirectPort(@Mock factoryMock: TomcatServletWebServerFactory) {
        // given
        serverProperties.port = 420

        // when
        testSubject.customize(factoryMock)

        // then
        val connectorCaptor = ArgumentCaptor.forClass(Connector::class.java)

        then(factoryMock).should().addAdditionalTomcatConnectors(connectorCaptor.capture())
        assertThat(connectorCaptor.value.redirectPort).isEqualTo(420)
    }

    @Test
    fun customizeAddsConnectorWithExpectedPort(@Mock factoryMock: TomcatServletWebServerFactory) {
        // given
        httpsRedirectProperties.port = 1337

        // when
        testSubject.customize(factoryMock)

        // then
        val connectorCaptor = ArgumentCaptor.forClass(Connector::class.java)

        then(factoryMock).should().addAdditionalTomcatConnectors(connectorCaptor.capture())
        assertThat(connectorCaptor.value.port).isEqualTo(1337)
    }

    @Test
    fun customizeAddsCustomizerEnforcingHttpsForAllConnections(@Mock factoryMock: TomcatServletWebServerFactory,
                                                               @Mock contextMock: Context) {
        // given
        willAnswer { invocation ->
            invocation.getArgument(0, TomcatContextCustomizer::class.java).customize(contextMock)
            null
        }.given(factoryMock).addContextCustomizers(any())

        // when
        testSubject.customize(factoryMock)

        // then
        val securityConstraintCaptor = ArgumentCaptor.forClass(SecurityConstraint::class.java)
        then(contextMock).should().addConstraint(securityConstraintCaptor.capture())

        assertSoftly { softly ->
            val addedSecurityConstraint = securityConstraintCaptor.value

            softly.assertThat(addedSecurityConstraint.userConstraint).isEqualTo("CONFIDENTIAL")
            softly.assertThat(addedSecurityConstraint.findCollections().asList()).hasSize(1)
                    .first()
                    .extracting { it.findPatterns().asList() }
                    .asList()
                    .containsExactly("/web/*")
        }
    }

}
