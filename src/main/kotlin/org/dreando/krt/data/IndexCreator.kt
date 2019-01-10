package org.dreando.krt.data

import com.mongodb.client.model.Indexes
import org.bson.conversions.Bson
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.toMono
import javax.annotation.PostConstruct

@Component
@Profile("index-db")
class IndexCreator(private val reactiveMongoOperations: ReactiveMongoOperations) {

    private val logger = LoggerFactory.getLogger(IndexCreator::class.java)

    @PostConstruct
    fun init() {
        createEventIndex(Indexes.geo2dsphere("point"))
    }

    private fun createEventIndex(index: Bson) {
        reactiveMongoOperations.getCollection("event")
                .createIndex(index)
                .toMono()
                .subscribe { message ->
                    logger.info("Indexed, $message")
                }
    }
}