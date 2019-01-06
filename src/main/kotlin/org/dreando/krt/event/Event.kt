package org.dreando.krt.event

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Event(val name: String, val tags: List<Tag>, val point: GeoJsonPoint)

data class Tag(val name: String)