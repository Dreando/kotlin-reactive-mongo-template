package org.dreando.krt.event

import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class EventRepository(private val reactiveMongoOperations: ReactiveMongoOperations) {

    fun insert(events: List<Event>) {
        reactiveMongoOperations
                .insertAll(events)
                .subscribe()
    }

    fun insert(event: Event) {
        reactiveMongoOperations
                .insert(event)
                .subscribe()
    }

    fun find(eventQueryOptions: EventQueryOptions): Flux<Event> {
        return reactiveMongoOperations.find(sortPageableCriteriaQuery(eventQueryOptions), Event::class.java)
    }

    fun count(eventQueryOptions: EventQueryOptions): Mono<Long> {
        return reactiveMongoOperations.count(criteriaQuery(eventQueryOptions), Event::class.java)
    }

    private fun sortPageableCriteriaQuery(eventQueryOptions: EventQueryOptions): Query {
        return criteriaQuery(eventQueryOptions).apply {
            eventQueryOptions.sort?.let {
                this.with(it)
            }
            eventQueryOptions.pageable?.let {
                this.with(it)
            }
        }
    }

    private fun criteriaQuery(eventQueryOptions: EventQueryOptions): Query {
        return Query().apply {
            eventQueryOptions.criteria().forEach { criteria ->
                this.addCriteria(criteria)
            }
        }
    }
}