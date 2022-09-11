package dev.mieser.tsa.testutil

import org.apache.commons.io.input.CloseShieldInputStream
import org.bouncycastle.util.io.pem.PemReader
import java.io.InputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.DSAPrivateKey
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec

/**
 * Util class for loading self-signed certificates which can be used in tests.
 */
class TestCertificateLoader {

    companion object {

        /**
         * An RSA certificate which has an *Extended Key Usage* Extension marked as critical which contains
         * `id-kp-timeStamping` as its only *KeyPurposeId*.
         */
        @JvmStatic
        val rsaCertificate: X509Certificate by lazy {
            loadCertificateFromClasspath("/rsa/cert.pem")
        }

        /**
         * The corresponding RSA private key.
         */
        @JvmStatic
        val rsaPrivateKey: RSAPrivateKey by lazy {
            loadPrivateKeyFromClasspath("/rsa/key.pem", "RSA")
        }

        /**
         * An EC certificate which has an *Extended Key Usage* Extension marked as critical which contains
         * `id-kp-timeStamping` as its only *KeyPurposeId*.
         */
        @JvmStatic
        val ecCertificate: X509Certificate by lazy {
            loadCertificateFromClasspath("/ec/cert.pem")
        }

        /**
         * The corresponding EC private key.
         */
        @JvmStatic
        val ecPrivateKey: ECPrivateKey by lazy {
            loadPrivateKeyFromClasspath("/ec/key.pem", "EC")
        }

        /**
         * A DSA certificate which has an *Extended Key Usage* Extension marked as critical which contains
         * `id-kp-timeStamping` as its only *KeyPurposeId*.
         */
        @JvmStatic
        val dsaCertificate: X509Certificate by lazy {
            loadCertificateFromClasspath("/dsa/cert.pem")

        }

        /**
         * The corresponding DSA private key.
         */
        @JvmStatic
        val dsaPrivateKey: DSAPrivateKey by lazy {
            loadPrivateKeyFromClasspath("/dsa/key.pem", "DSA")
        }

        private fun loadCertificateFromClasspath(path: String): X509Certificate {
            return loadResourceFromClasspath(path) { certificateStream ->
                val certFactory = CertificateFactory.getInstance("X.509")
                certFactory.generateCertificate(certificateStream) as X509Certificate
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T : PrivateKey> loadPrivateKeyFromClasspath(path: String, keyAlgorithmName: String): T {
            return loadResourceFromClasspath(path) { privateKeyStream ->
                val keyFactory = KeyFactory.getInstance(keyAlgorithmName)

                CloseShieldInputStream.wrap(privateKeyStream).reader().use { privateKeyReader ->
                    val pemReader = PemReader(privateKeyReader)
                    val privateKeyBytes = pemReader.readPemObject().content
                    val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
                    keyFactory.generatePrivate(privateKeySpec) as T
                }
            }
        }

        private fun <T> loadResourceFromClasspath(path: String, streamConsumer: (InputStream) -> T): T {
            return TestCertificateLoader::class.java.getResourceAsStream(path).use { inputStream -> streamConsumer(inputStream!!) }
        }

    }

}
