package dev.mieser.tsa.web.formatter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.io.IOException
import java.math.BigInteger
import java.util.Locale

/**
 * Jackson [JsonSerializer] to serialize [BigInteger]s as uppercase Hexadecimal Strings.
 */
class HexJsonSerializer : StdSerializer<BigInteger>(BigInteger::class.java) {

    @Throws(IOException::class)
    override fun serialize(value: BigInteger?, jsonGenerator: JsonGenerator,
                           serializerProvider: SerializerProvider) {
        if (value == null) {
            jsonGenerator.writeNull()
        } else {
            jsonGenerator.writeString(value.toString(HEX_RADIX).uppercase(Locale.getDefault()))
        }
    }

    companion object {
        private const val HEX_RADIX = 16
    }
}
