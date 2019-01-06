package org.dreando.krt.event

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class EventQueryService(private val eventQueryOptionsBuilder: EventQueryOptionsBuilder,
                        private val eventRepository: EventRepository) {

    private val logger = LoggerFactory.getLogger(EventQueryService::class.java)

    fun query(queryParams: MultiValueMap<String, String>): Flux<Event> {
        val eventQueryOptions = eventQueryOptionsBuilder.build(queryParams)
        return eventRepository.select(eventQueryOptions).also {
            logger.info("Going to query for $eventQueryOptions")
        }
    }

    fun count(queryParams: MultiValueMap<String, String>): Mono<Long> {
        val eventQueryOptions = eventQueryOptionsBuilder.build(queryParams)
        return eventRepository.count(eventQueryOptions).also {
            logger.info("Going to count for $eventQueryOptions")
        }
    }
}