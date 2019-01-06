package org.dreando.krt.event

import org.dreando.krt.config.QueryParams
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class EventQueryService(private val eventQueryParamsBuilder: EventQueryParamsBuilder,
                        private val eventRepository: EventRepository) {

    fun query(queryQueryParams: QueryParams): Flux<Event> {
        return eventRepository.find(eventQueryParamsBuilder.build(queryQueryParams))
    }

    fun count(queryQueryParams: QueryParams): Mono<Long> {
        return eventRepository.count(eventQueryParamsBuilder.build(queryQueryParams))
    }
}