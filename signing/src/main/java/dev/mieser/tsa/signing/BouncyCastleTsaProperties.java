package dev.mieser.tsa.signing;

import static dev.mieser.tsa.domain.HashAlgorithm.SHA256;

import java.util.EnumSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import dev.mieser.tsa.domain.HashAlgorithm;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BouncyCastleTsaProperties {

    /**
     * The hash algorithm which is used to calculate the TSA's certificate identifier ({@code ESSCertIDv2}).
     * <p/>
     * {@link HashAlgorithm#SHA256} is used by default. Cannot be {@code null}.
     */
    @NotNull
    private HashAlgorithm essCertIdAlgorithm = SHA256;

    /**
     * The hash algorithm which is used to calculate the TSP requests digest, which will be signed by the TSA. A JCA
     * Provider must be registered which provides an algorithm called {@code XwithY} where {@code X} is the value of this
     * property and {@code Y} is the name of the algorithm of the public key used to sign the TSP requests.
     * <p/>
     * {@link HashAlgorithm#SHA256} is used by default. Cannot be {@code null}
     */
    @NotNull
    private HashAlgorithm signingDigestAlgorithm = SHA256;

    /**
     * The Hash Algorithms which are accepted by the Timestamp Authority. A TSP request specifying a known
     * {@link HashAlgorithm} which is not part of this set will be rejected.
     * <p/>
     * All known {@link HashAlgorithm}s are accepted by default. Cannot be empty.
     */
    @NotEmpty
    private Set<HashAlgorithm> acceptedHashAlgorithms = EnumSet.allOf(HashAlgorithm.class);

    /**
     * The OID of the policy under which the TSP responses are produced.
     * <p/>
     * Default is set to OID {@code 1.2}. Cannot be empty.
     */
    @NotEmpty
    private String policyOid = "1.2";

}
