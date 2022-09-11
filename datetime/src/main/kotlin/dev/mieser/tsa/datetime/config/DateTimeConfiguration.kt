package dev.mieser.tsa.datetime.config

import dev.mieser.tsa.datetime.CurrentDateServiceImpl
import dev.mieser.tsa.datetime.DateConverterImpl
import dev.mieser.tsa.datetime.api.CurrentDateService
import dev.mieser.tsa.datetime.api.DateConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZoneId

@Configuration
internal open class DateTimeConfiguration {
    /**
     * @return
     */
    @Bean
    open fun currentDateService(): CurrentDateService = CurrentDateServiceImpl(Clock.systemDefaultZone())

    /**
     * @return
     */
    @Bean
    open fun dateConverter(): DateConverter = DateConverterImpl(ZoneId.systemDefault())
}
