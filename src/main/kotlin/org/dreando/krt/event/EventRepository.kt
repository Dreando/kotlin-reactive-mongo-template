package org.dreando.krt.event

import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class EventRepository(private val mongoOperations: ReactiveMongoOperations,
                      private val blockingMongoOperations: MongoOperations) {

    fun insert(events: List<Event>) {
        blockingMongoOperations.insertAll(events)
    }

    fun insert(event: Event) {
        mongoOperations
                .insert(event)
                .subscribe()
    }

    fun find(eventQueryOptions: EventQueryOptions): Flux<Event> {
        return mongoOperations.find(sortPageableCriteriaQueryOf(eventQueryOptions), Event::class.java)
    }

    fun count(eventQueryOptions: EventQueryOptions): Mono<Long> {
        return mongoOperations.count(criteriaQueryOf(eventQueryOptions), Event::class.java)
    }

    private fun sortPageableCriteriaQueryOf(eventQueryOptions: EventQueryOptions): Query {
        return criteriaQueryOf(eventQueryOptions).apply {
            eventQueryOptions.sort?.let {
                this.with(it)
            }
            eventQueryOptions.pageable?.let {
                this.with(it)
            }
        }
    }

    private fun criteriaQueryOf(eventQueryOptions: EventQueryOptions): Query {
        return Query().apply {
            eventQueryOptions.criteria().forEach { criteria ->
                this.addCriteria(criteria)
            }
        }
    }
}