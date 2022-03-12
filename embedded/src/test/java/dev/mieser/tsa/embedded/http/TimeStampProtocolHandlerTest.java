package dev.mieser.tsa.embedded.http;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.mieser.tsa.signing.api.TimeStampAuthority;

@ExtendWith(MockitoExtension.class)
class TimeStampProtocolHandlerTest {

    private final TimeStampAuthority timeStampAuthorityMock;

    private final TimeStampProtocolHandler testSubject;

    TimeStampProtocolHandlerTest(@Mock TimeStampAuthority timeStampAuthorityMock) {
        this.timeStampAuthorityMock = timeStampAuthorityMock;
        this.testSubject = new TimeStampProtocolHandler(timeStampAuthorityMock);
    }

    @Test
    void answersWithBadRequestWhenWrongContentTypeSpecified(@Mock Request requestMock, @Mock Response responseMock) {
        // given
        given(requestMock.getContentType()).willReturn("text/plain");

        // when
        testSubject.service(requestMock, responseMock);

        // then
        then(responseMock).should().setStatus(HttpStatus.BAD_REQUEST_400);
    }

}
