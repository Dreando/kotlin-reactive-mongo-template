package org.dreando.krt.event

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.geo.Box
import org.springframework.data.geo.Circle
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.CriteriaDefinition

data class EventQueryOptions(val pageable: Pageable?,
                             val sort: Sort?,
                             val nameLike: String?,
                             val boundingSphere: Circle?,
                             val boundingBox: Box?,
                             val tags: List<String>)

fun EventQueryOptions.criteria(): List<CriteriaDefinition> {

    val criteria = mutableListOf<CriteriaDefinition>()

    nameLike?.let { criteria.add(Criteria.where("name").regex(it)) }
    boundingSphere?.let { criteria.add(Criteria.where("point").withinSphere(it)) }
    boundingBox?.let { criteria.add(Criteria.where("point").within(it)) }
    if (tags.isNotEmpty()) criteria.add(Criteria.where("tags.name").all(tags))

    return criteria
}