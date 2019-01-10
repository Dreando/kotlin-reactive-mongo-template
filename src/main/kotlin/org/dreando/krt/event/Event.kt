package org.dreando.krt.event

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
data class Event(val name: String,
                 val tags: List<Tag>,
                 val lastsFrom: LocalDate,
                 val lastsTo: LocalDate,
                 val point: GeoJsonPoint)

data class Tag(val name: String)