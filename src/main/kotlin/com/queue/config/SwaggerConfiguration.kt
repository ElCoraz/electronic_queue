package com.queue.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.VendorExtension
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun apiDocket(): Docket {
        val contact = Contact(
                "ElCorazon",
                "",
                "dmwhite19850908@gmail.com"
        )
        val vendorExtensions: List<VendorExtension<*>> = ArrayList()
        val apiInfo = ApiInfo(
                "Документация RESTful FE",
                "Описание RESTful сервиса FE", "1.0.0",
                "http://www.domen.com/service.html", contact,
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", vendorExtensions)
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.application.execute.controller"))
                .paths(PathSelectors.any())
                .build()
    }
}