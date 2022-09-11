package dev.mieser.tsa.web.config

import dev.mieser.tsa.web.formatter.Base64Formatter
import dev.mieser.tsa.web.formatter.HexFormatter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.Locale

@Configuration
internal open class CustomWebMvcConfiguration : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatter(Base64Formatter())
        registry.addFormatter(HexFormatter())
    }

    @Bean
    open fun localeResolver(): LocaleResolver {
        val localeResolver = AcceptHeaderLocaleResolver()
        localeResolver.defaultLocale = Locale.ENGLISH
        return localeResolver
    }

}
