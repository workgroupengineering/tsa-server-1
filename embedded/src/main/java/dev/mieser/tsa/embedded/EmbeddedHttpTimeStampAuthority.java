package dev.mieser.tsa.embedded;

import java.io.IOException;
import java.time.Clock;
import java.time.ZoneId;
import java.util.function.Function;

import org.glassfish.grizzly.PortRange;
import org.glassfish.grizzly.http.server.HttpServer;

import dev.mieser.tsa.datetime.CurrentDateTimeServiceImpl;
import dev.mieser.tsa.datetime.DateConverterImpl;
import dev.mieser.tsa.embedded.http.TimeStampProtocolHandler;
import dev.mieser.tsa.signing.BouncyCastleTimeStampAuthority;
import dev.mieser.tsa.signing.BouncyCastleTsaProperties;
import dev.mieser.tsa.signing.TspParser;
import dev.mieser.tsa.signing.TspValidator;
import dev.mieser.tsa.signing.api.TimeStampAuthority;
import dev.mieser.tsa.signing.cert.ClasspathCertificateLoader;
import dev.mieser.tsa.signing.cert.PublicKeyAnalyzer;
import dev.mieser.tsa.signing.mapper.TimeStampResponseMapper;
import dev.mieser.tsa.signing.serial.RandomSerialNumberGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmbeddedHttpTimeStampAuthority implements AutoCloseable {

    /**
     * The dynamic TCP port range as defined by <a href="https://tools.ietf.org/html/rfc6335">RFC 6335</a>.
     */
    static final PortRange DYNAMIC_PORT_RANGE = new PortRange(49_152, 65_535);

    /**
     * Abstraction of the static {@link HttpServer#createSimpleServer(String, PortRange)} method to return mocks in
     * unit-tests.
     */
    private final Function<PortRange, HttpServer> httpServerFactory;

    private final HttpServer httpServer;

    private final TimeStampAuthority timeStampAuthority;

    public EmbeddedHttpTimeStampAuthority() {
        this(portRange -> HttpServer.createSimpleServer(".", portRange));
    }

    EmbeddedHttpTimeStampAuthority(Function<PortRange, HttpServer> httpServerFactory) {
        this.httpServerFactory = httpServerFactory;
        this.timeStampAuthority = configureTsa();
        this.httpServer = configureServer(timeStampAuthority);
    }

    /**
     * Starts the embedded HTTP server.
     */
    public void start() {
        try {
            timeStampAuthority.initialize();
            httpServer.start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start the embedded HTTP server.", e);
        }
    }

    /**
     * Stops the embedded HTTP server.
     */
    public void stop() {
        httpServer.shutdownNow();
    }

    /**
     * @return The TCP port the embedded HTTP Server is bound to.
     * @throws IllegalStateException
     *     When the embedded HTTP server is not running.
     */
    public int getPort() {
        if (!httpServer.isStarted()) {
            throw new IllegalStateException("The embedded HTTP server is not started.");
        }

        return httpServer.getListeners().iterator().next().getPort();
    }

    /**
     * @see #stop()
     */
    @Override
    public void close() {
        stop();
    }

    private HttpServer configureServer(TimeStampAuthority timeStampAuthority) {
        HttpServer server = httpServerFactory.apply(DYNAMIC_PORT_RANGE);
        server.getServerConfiguration().addHttpHandler(new TimeStampProtocolHandler(timeStampAuthority));

        return server;
    }

    private TimeStampAuthority configureTsa() {
        return new BouncyCastleTimeStampAuthority(new BouncyCastleTsaProperties(), new TspParser(), new TspValidator(),
            new ClasspathCertificateLoader("dev/mieser/tsa/embedded/rsa.p12", new char[0]),
            new CurrentDateTimeServiceImpl(Clock.systemDefaultZone()), new RandomSerialNumberGenerator(),
            new TimeStampResponseMapper(new DateConverterImpl(ZoneId.systemDefault())), new PublicKeyAnalyzer());
    }

}
