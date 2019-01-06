package org.dreando.krt

import org.dreando.krt.config.beans
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class KotlinReactiveTemplateApplication

fun main(args: Array<String>) {
    val springApplication = SpringApplicationBuilder()
            .initializers(beans())
            .web(WebApplicationType.REACTIVE)
            .sources(KotlinReactiveTemplateApplication::class.java)
            .build(*args)

    springApplication.run()
}

