package dev.mieser.tsa.signing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;

import dev.mieser.tsa.datetime.api.CurrentDateTimeService;
import dev.mieser.tsa.datetime.api.DateConverter;
import dev.mieser.tsa.datetime.config.DateTimeConfiguration;
import dev.mieser.tsa.signing.BouncyCastleTimeStampAuthority;
import dev.mieser.tsa.signing.BouncyCastleTimeStampValidator;
import dev.mieser.tsa.signing.TspParser;
import dev.mieser.tsa.signing.TspValidator;
import dev.mieser.tsa.signing.api.TimeStampAuthority;
import dev.mieser.tsa.signing.api.TimeStampValidator;
import dev.mieser.tsa.signing.cert.Pkcs12SigningCertificateLoader;
import dev.mieser.tsa.signing.cert.PublicKeyAnalyzer;
import dev.mieser.tsa.signing.cert.SigningCertificateExtractor;
import dev.mieser.tsa.signing.cert.SigningCertificateLoader;
import dev.mieser.tsa.signing.mapper.TimeStampResponseMapper;
import dev.mieser.tsa.signing.mapper.TimeStampValidationResultMapper;
import dev.mieser.tsa.signing.serial.RandomSerialNumberGenerator;

@Configuration
@Import(DateTimeConfiguration.class)
@EnableConfigurationProperties(TsaProperties.class)
public class TsaConfiguration {

    @Bean
    TimeStampAuthority timeStampAuthority(TsaProperties tsaProperties, SigningCertificateLoader signingCertificateLoader,
        CurrentDateTimeService currentDateTimeService, DateConverter dateConverter) {
        return new BouncyCastleTimeStampAuthority(tsaProperties, tspParser(), tspValidator(),
            signingCertificateLoader, currentDateTimeService, new RandomSerialNumberGenerator(),
            new TimeStampResponseMapper(dateConverter),
            publicKeyAnalyzer());
    }

    @Bean
    TimeStampValidator timeStampValidator(SigningCertificateLoader signingCertificateLoader, DateConverter dateConverter) {
        return new BouncyCastleTimeStampValidator(tspParser(), signingCertificateLoader, publicKeyAnalyzer(),
            new TimeStampValidationResultMapper(dateConverter), tspValidator(), signingCertificateExtractor());
    }

    @Bean
    PublicKeyAnalyzer publicKeyAnalyzer() {
        return new PublicKeyAnalyzer();
    }

    @Bean
    TspParser tspParser() {
        return new TspParser();
    }

    @Bean
    TspValidator tspValidator() {
        return new TspValidator();
    }

    @Bean
    SigningCertificateExtractor signingCertificateExtractor() {
        return new SigningCertificateExtractor();
    }

    @Bean
    SigningCertificateLoader signingCertificateLoader(TsaProperties tsaProperties, ResourceLoader resourceLoader) {
        char[] password = toCharArray(tsaProperties.getCertificate().getPassword());
        String path = tsaProperties.getCertificate().getPath();

        return new Pkcs12SigningCertificateLoader(resourceLoader, path, password);
    }

    private char[] toCharArray(String password) {
        return password != null ? password.toCharArray() : new char[0];
    }

}
