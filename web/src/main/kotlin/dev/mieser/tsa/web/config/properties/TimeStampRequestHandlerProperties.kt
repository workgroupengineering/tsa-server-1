package dev.mieser.tsa.web.config.properties

import dev.mieser.tsa.web.validator.TcpPort
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "tsa.server")
class TimeStampRequestHandlerProperties {

    /**
     * The TCP port of the Tomcat Connector which handles Time Stamp Query HTTP requests. Defaults to port `318`.
     *
     * Must be a value between `1` and `65535` (inclusive) which differs from the default Tomcat Connector port
     * used for other requests.
     */
    @TcpPort
    var port: Int = 318

}
