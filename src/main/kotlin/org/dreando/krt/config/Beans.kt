package org.dreando.krt.config

import org.dreando.krt.event.eventRouter
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

fun beans(): BeanDefinitionDsl {
    return beans {
        eventRouter()
    }
}