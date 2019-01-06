package org.dreando.krt.event

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.geo.Circle
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.CriteriaDefinition
import org.springframework.data.mongodb.core.query.isEqualTo

data class EventQueryOptions(val pageNumber: Int?,
                             val pageSize: Int?,
                             val sort: List<String>,
                             val sortDirection: Sort.Direction?,
                             val nameLike: String?,
                             val boundingSphere: Circle?,
                             val tags: List<String>)

fun EventQueryOptions.pageable(): Pageable? {
    return if (pageNumber != null && pageSize != null) PageRequest.of(pageNumber, pageSize) else null
}

fun EventQueryOptions.sort(): Sort? {
    return if (sort.isNotEmpty() && sortDirection != null) Sort.by(sortDirection, *sort.toTypedArray()) else null
}

fun EventQueryOptions.criteria(): List<CriteriaDefinition> {

    val criteria = mutableListOf<CriteriaDefinition>()

    nameLike?.let { criteria.add(Criteria.where("name").regex(it)) }
    boundingSphere?.let { criteria.add(Criteria.where("point").withinSphere(it)) }

    if (tags.isNotEmpty()) {
        criteria.add(Criteria.where("tags.name").all(tags))
    }

    return criteria
}