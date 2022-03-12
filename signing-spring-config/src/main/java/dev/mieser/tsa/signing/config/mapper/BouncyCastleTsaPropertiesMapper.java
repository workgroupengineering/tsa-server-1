package dev.mieser.tsa.signing.config.mapper;

import org.mapstruct.Mapper;

import dev.mieser.tsa.signing.BouncyCastleTsaProperties;
import dev.mieser.tsa.signing.config.TsaConfigurationProperties;

/**
 * <a href="https://mapstruct.org">Mapstruct</a> Mapper to map the Spring-accessible {@link TsaConfigurationProperties}
 * to the API implementation internal {@link BouncyCastleTsaProperties}. This allows proper encapsulation of API
 * implementation internals.
 */
@Mapper
public interface BouncyCastleTsaPropertiesMapper {

    BouncyCastleTsaProperties map(TsaConfigurationProperties properties);

}
