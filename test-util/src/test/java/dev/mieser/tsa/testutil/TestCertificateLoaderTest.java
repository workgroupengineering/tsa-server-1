package dev.mieser.tsa.testutil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestCertificateLoaderTest {

    @ParameterizedTest
    @MethodSource("methodsToInvoke")
    void canLoadCertificateOrPrivateKey(ThrowingSupplier<?> methodInvocation) throws Throwable {
        // given / when / then
        assertThat(methodInvocation.get()).isNotNull();
    }

    static Stream<Arguments> methodsToInvoke() {
        return Stream.of(
            arguments((ThrowingSupplier<Object>) TestCertificateLoader::getRsaCertificate),
            arguments((ThrowingSupplier<Object>) TestCertificateLoader::getRsaPrivateKey),
            arguments((ThrowingSupplier<Object>) TestCertificateLoader::getDsaCertificate),
            arguments((ThrowingSupplier<Object>) TestCertificateLoader::getDsaPrivateKey),
            arguments((ThrowingSupplier<Object>) TestCertificateLoader::getEcCertificate),
            arguments((ThrowingSupplier<Object>) TestCertificateLoader::getEcPrivateKey));
    }

}
