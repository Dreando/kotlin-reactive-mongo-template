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

    fun select(eventQueryOptions: EventQueryOptions): Flux<Event> {

        val query = Query().apply {

            eventQueryOptions.criteria().forEach { criteria ->
                this.addCriteria(criteria)
            }

            eventQueryOptions.sort()?.let {
                this.with(it)
            }

            eventQueryOptions.pageable()?.let {
                this.with(it)
            }
        }


        return reactiveMongoOperations.find(query, Event::class.java)
    }

    fun count(eventQueryOptions: EventQueryOptions): Mono<Long> {
        val query = Query().apply {
            eventQueryOptions.criteria().forEach { criteria ->
                this.addCriteria(criteria)
            }
        }
        return reactiveMongoOperations.count(query, Event::class.java)
    }
}