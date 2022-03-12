package dev.mieser.tsa.signing.config.mapper;

import static dev.mieser.tsa.domain.HashAlgorithm.SHA1;
import static dev.mieser.tsa.domain.HashAlgorithm.SHA512;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import dev.mieser.tsa.signing.BouncyCastleTsaProperties;
import dev.mieser.tsa.signing.config.TsaConfigurationProperties;

class BouncyCastleTsaPropertiesMapperTest {

    private final BouncyCastleTsaPropertiesMapper testSubject = Mappers.getMapper(BouncyCastleTsaPropertiesMapper.class);

    @Test
    void mapsReturnsExpectedProperties() {
        // given
        var sourceCertificateLoaderProperties = new TsaConfigurationProperties.CertificateLoaderProperties()
            .setPassword("password")
            .setPath("path");

        var sourceProperties = new TsaConfigurationProperties()
            .setAcceptedHashAlgorithms(EnumSet.of(SHA1, SHA512))
            .setEssCertIdAlgorithm(SHA1)
            .setPolicyOid("1.2.3.4")
            .setSigningDigestAlgorithm(SHA512)
            .setCertificate(sourceCertificateLoaderProperties);

        // when
        BouncyCastleTsaProperties mappedProperties = testSubject.map(sourceProperties);

        // then
        var expectedCertificateLoaderProperties = new BouncyCastleTsaProperties.CertificateLoaderProperties()
            .setPassword("password")
            .setPath("path");

        var expectedProperties = new BouncyCastleTsaProperties()
            .setAcceptedHashAlgorithms(EnumSet.of(SHA1, SHA512))
            .setEssCertIdAlgorithm(SHA1)
            .setPolicyOid("1.2.3.4")
            .setSigningDigestAlgorithm(SHA512)
            .setCertificate(expectedCertificateLoaderProperties);

        assertThat(mappedProperties).isEqualTo(expectedProperties);
    }

}
