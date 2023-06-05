package com.queue.config

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableAutoConfiguration
class ThymeleafConfig {

    @Bean
    fun layoutDialect(): LayoutDialect {
        return LayoutDialect()
    }
}