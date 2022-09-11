package dev.mieser.tsa.web.config

import dev.mieser.tsa.web.config.properties.HttpsRedirectProperties
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Configuration

/**
 * [WebServerFactoryCustomizer] to add a Tomcat [Connector] which redirects all traffic to the default
 * Connector configured by Spring's `server.*` properties.
 */
@Configuration
@EnableConfigurationProperties(HttpsRedirectProperties::class)
@ConditionalOnProperty(name = ["server.ssl.key-store"])
internal open class HttpsRedirectConfiguration(private val httpsRedirectProperties: HttpsRedirectProperties, private val serverProperties: ServerProperties) : WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    override fun customize(factory: TomcatServletWebServerFactory) {
        factory.addAdditionalTomcatConnectors(redirectConnector())
        factory.addContextCustomizers(confidentialSecurityConstraintContextCustomizer())
    }

    /**
     * @return A [TomcatContextCustomizer] adding a [SecurityConstraint] which enforces `HTTPS` for all
     * frontend requests.
     */
    private fun confidentialSecurityConstraintContextCustomizer(): TomcatContextCustomizer {
        return TomcatContextCustomizer { context ->
            val securityCollection = SecurityCollection().apply {
                addPattern("/web/*")
            }
            val securityConstraint = SecurityConstraint().apply {
                userConstraint = "CONFIDENTIAL"
                addCollection(securityCollection)
            }
            context.addConstraint(securityConstraint)
        }
    }

    /**
     * @return A Tomcat [Connector] redirecting all requests to the default Spring default Connector configured by
     * Spring's `server.*` properties.
     */
    private fun redirectConnector(): Connector {
        return Connector().apply {
            port = httpsRedirectProperties.port
            redirectPort = serverPort()
        }
    }

    /**
     * @return The configured default Tomcat Connector port.
     * @implNote [ServerProperties.getPort] returns `null` when the `server.port` property is not set
     * explicitly.
     */
    private fun serverPort(): Int {
        return serverProperties.port ?: DEFAULT_SERVER_PORT
    }

    companion object {

        /**
         * The default Spring server port when the `server.port` property is not set explicitly.
         */
        private const val DEFAULT_SERVER_PORT = 8080

    }

}
