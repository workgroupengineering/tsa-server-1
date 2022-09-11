package dev.mieser.tsa.web.formatter

import org.apache.commons.codec.binary.Base64
import org.springframework.format.Formatter
import java.util.Locale

/**
 * [Formatter] to encode/decode byte arrays to/from Base64 Strings.
 */
class Base64Formatter : Formatter<ByteArray> {

    override fun parse(text: String, locale: Locale): ByteArray {
        require(Base64.isBase64(text)) { "Not a valid Base64 string." }
        return Base64.decodeBase64(text)
    }

    override fun print(array: ByteArray, locale: Locale): String {
        return Base64.encodeBase64String(array)
    }

}
