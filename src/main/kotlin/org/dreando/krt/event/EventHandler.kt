package org.dreando.krt.event

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Component
class EventHandler(private val eventQueryService: EventQueryService) {

    fun selectWithParams(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse
                .ok()
                .body(eventQueryService.query(serverRequest.queryParams()))
    }

    fun countWithParams(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse
                .ok()
                .body(eventQueryService.count(serverRequest.queryParams()))
    }
}