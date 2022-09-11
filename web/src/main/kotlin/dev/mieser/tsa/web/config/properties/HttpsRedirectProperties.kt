package dev.mieser.tsa.web.config.properties

import dev.mieser.tsa.web.validator.TcpPort
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "tsa.http")
class HttpsRedirectProperties {

    /**
     * The TCP port of the Tomcat Connector which redirects all incoming requests to the default connector configured by
     * Spring's `server.*` Properties.
     *
     * Defaults to port `80`. Must be a value between `1` and `65535` (inclusive) which differs from the default Tomcat Connector port
     * used for other requests.
     */
    @TcpPort
    var port: Int = 80

}
