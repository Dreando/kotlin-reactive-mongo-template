package org.dreando.krt.data

import com.mongodb.client.model.Indexes
import org.dreando.krt.event.Event
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver
import org.springframework.stereotype.Component
import reactor.core.publisher.toMono
import javax.annotation.PostConstruct

@Component
@Profile("init-db")
class IndexCreator(private val reactiveMongoOperations: ReactiveMongoOperations) {

    @PostConstruct
    fun init() {
        reactiveMongoOperations.getCollection("event")
                .createIndex(Indexes.geo2dsphere("point"))
                .toMono()
                .subscribe()
    }
}