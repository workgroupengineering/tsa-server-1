package dev.mieser.tsa.web.config

import dev.mieser.tsa.web.config.properties.TimeStampRequestHandlerProperties
import dev.mieser.tsa.web.filter.TimeStampProtocolFilter
import org.apache.catalina.connector.Connector
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter

/**
 * [WebServerFactoryCustomizer] to add a Tomcat Connector especially for handling Time Stamp Protocol requests. A
 * [TimeStampProtocolFilter] is bound to the same port to prevent the connector from responding to other requests.
 */
@Configuration
@EnableConfigurationProperties(TimeStampRequestHandlerProperties::class)
internal open class TimeStampRequestHandlerConfiguration(private val timeStampRequestHandlerProperties: TimeStampRequestHandlerProperties) : WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    override fun customize(factory: TomcatServletWebServerFactory) {
        val connector = Connector().apply {
            port = timeStampRequestHandlerProperties.port
        }
        factory.addAdditionalTomcatConnectors(connector)
    }

    @Bean
    open fun timeStampProtocolFilter(): Filter {
        return TimeStampProtocolFilter(timeStampRequestHandlerProperties.port)
    }

}
