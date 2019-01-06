package org.dreando.krt.event

import org.dreando.krt.config.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EventQueryParamsBuilder {

    private val logger = LoggerFactory.getLogger(EventQueryParamsBuilder::class.java)

    fun build(queryParams: QueryParams): EventQueryOptions {
        return EventQueryOptions(
                pageable = queryParams.getPageable(),
                sort = queryParams.getSort(),
                boundingSphere = queryParams.getBoundingSphere(),
                boundingBox = queryParams.getBoundingBox(),
                nameLike = queryParams.getString("nameLike"),
                tags = queryParams.getStrings("tag")
        ).also { eventQueryOptions ->
            logger.info("Built $eventQueryOptions from $queryParams")
        }
    }
}