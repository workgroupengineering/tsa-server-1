package dev.mieser.tsa.web.formatter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigInteger
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
internal class HexJsonSerializerTest {

    private val testSubject = HexJsonSerializer()

    @Test
    fun serializeWritesNullWhenValueIsNull(@Mock jsonGeneratorMock: JsonGenerator,
                                           @Mock serializerProviderMock: SerializerProvider) {
        // given / when
        testSubject.serialize(null, jsonGeneratorMock, serializerProviderMock)

        // then
        then(jsonGeneratorMock).should().writeNull()
    }

    @ParameterizedTest
    @MethodSource("longToHex")
    fun serializeWritesUppercaseHexStringWhenValueIsNotNull(longValue: Long, hexValue: String,
                                                            @Mock jsonGeneratorMock: JsonGenerator, @Mock serializerProviderMock: SerializerProvider) {
        // given / when
        testSubject.serialize(BigInteger.valueOf(longValue), jsonGeneratorMock, serializerProviderMock)

        // then
        then(jsonGeneratorMock).should().writeString(hexValue)
    }

    companion object {

        @JvmStatic
        fun longToHex(): Stream<Arguments> {
            return Stream.of(
                    Arguments.arguments(-1337L, "-539"),
                    Arguments.arguments(12, "C"),
                    Arguments.arguments(1515, "5EB"))
        }

    }

}
