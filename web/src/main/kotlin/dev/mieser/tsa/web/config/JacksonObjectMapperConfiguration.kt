package dev.mieser.tsa.web.config

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import dev.mieser.tsa.web.formatter.HexJsonSerializer
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
internal open class JacksonObjectMapperConfiguration : Jackson2ObjectMapperBuilderCustomizer {

    override fun customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder) {
        jacksonObjectMapperBuilder.modules(JavaTimeModule())
        jacksonObjectMapperBuilder.serializers(HexJsonSerializer())
    }

}
