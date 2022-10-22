package dev.mieser.tsa.signing.api;

import java.io.InputStream;

import dev.mieser.tsa.domain.TimeStampResponseData;

/**
 * Interface abstraction of a <a href="https://www.ietf.org/rfc/rfc3161.txt">RFC 3161</a> Time Stamp Authority.
 */
public interface TimeStampAuthority {

    /**
     * Initializes the Time Stamp Authority.
     *
     * @throws dev.mieser.tsa.signing.api.exception.TsaInitializationException
     *     When an error occurs while initializing the TSA.
     */
    void initialize();

    /**
     * @param tspRequestInputStream
     *     The input stream of an ASN.1 DER encoded TSP response, not {@code null}.
     * @return The TSP response data, including the ASN.1 DER encoded response.
     * @throws dev.mieser.tsa.signing.api.exception.TsaNotInitializedException
     *     When the TSA has not yet been {@link #initialize() initialized}.
     * @throws dev.mieser.tsa.signing.api.exception.UnknownHashAlgorithmException
     *     When the hash algorithm specified in the TSP request
     * @throws dev.mieser.tsa.signing.api.exception.InvalidTspRequestException
     *     When the specified input stream does not contain a valid ASN.1 DER encoded TSP request.
     * @throws dev.mieser.tsa.signing.api.exception.TspResponseException
     *     When an error occurs while generating the signing TSP request.
     */
    TimeStampResponseData signRequest(InputStream tspRequestInputStream);

}
