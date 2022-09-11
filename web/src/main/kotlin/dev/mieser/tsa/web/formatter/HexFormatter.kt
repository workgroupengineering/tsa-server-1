package dev.mieser.tsa.web.formatter

import org.apache.commons.lang3.StringUtils
import org.springframework.format.Formatter
import java.math.BigInteger
import java.util.Locale

/**
 * [Formatter] to represent [BigInteger]s as uppercase Hexadecimal Strings.
 */
class HexFormatter : Formatter<BigInteger?> {

    override fun parse(text: String, locale: Locale): BigInteger? {
        return if (StringUtils.isBlank(text)) {
            null
        } else {
            BigInteger(text, HEX_RADIX)
        }
    }

    override fun print(value: BigInteger?, locale: Locale): String? {
        return value?.toString(HEX_RADIX)?.uppercase(Locale.getDefault())
    }

    companion object {
        private const val HEX_RADIX = 16
    }

}
