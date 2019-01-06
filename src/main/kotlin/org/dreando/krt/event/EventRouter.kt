package org.dreando.krt.event

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.web.reactive.function.server.router

fun BeanDefinitionDsl.eventRouter() {
    bean("eventRouter") {
        router {
            ("/").nest {
                "/event".nest {
                    GET("/", ref<EventHandler>()::selectWithParams)
                    GET("/count", ref<EventHandler>()::countWithParams)
                }
            }
        }
    }
}