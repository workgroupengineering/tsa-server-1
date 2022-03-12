package dev.mieser.tsa.embedded;

import static dev.mieser.tsa.embedded.EmbeddedHttpTimeStampAuthority.DYNAMIC_PORT_RANGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.function.Function;

import org.glassfish.grizzly.PortRange;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmbeddedHttpTimeStampAuthorityTest {

    private final Function<PortRange, HttpServer> httpServerFactoryMock;

    EmbeddedHttpTimeStampAuthorityTest(@Mock Function<PortRange, HttpServer> httpServerFactoryMock) {
        this.httpServerFactoryMock = httpServerFactoryMock;
    }

    @Test
    void getPortThrowsExceptionWhenServerIsNotStarted(@Mock HttpServer httpServerMock,
        @Mock ServerConfiguration serverConfigurationMock) {
        // given
        given(httpServerFactoryMock.apply(DYNAMIC_PORT_RANGE)).willReturn(httpServerMock);
        given(httpServerMock.getServerConfiguration()).willReturn(serverConfigurationMock);
        given(httpServerMock.isStarted()).willReturn(false);

        var testSubject = new EmbeddedHttpTimeStampAuthority(httpServerFactoryMock);

        // when / then
        assertThatIllegalStateException()
            .isThrownBy(testSubject::getPort)
            .withMessage("The embedded HTTP server is not started.");
    }

    @Test
    void getPortReturnsPortOfFirstNetworkListener(@Mock HttpServer httpServerMock,
        @Mock ServerConfiguration serverConfigurationMock, @Mock NetworkListener networkListenerMock) {
        // given
        given(httpServerFactoryMock.apply(DYNAMIC_PORT_RANGE)).willReturn(httpServerMock);
        given(httpServerMock.getServerConfiguration()).willReturn(serverConfigurationMock);
        given(httpServerMock.isStarted()).willReturn(false);
        given(httpServerMock.getListeners()).willReturn(List.of(networkListenerMock));
        given(networkListenerMock.getPort()).willReturn(1337);

        var testSubject = new EmbeddedHttpTimeStampAuthority(httpServerFactoryMock);

        // when
        int actualPort = testSubject.getPort();

        // then
        assertThat(actualPort).isEqualTo(1337);
    }

}
