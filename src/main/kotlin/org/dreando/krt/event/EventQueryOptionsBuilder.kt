package org.dreando.krt.event

import org.dreando.krt.exception.WrongParamFormatException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics.KILOMETERS
import org.springframework.data.geo.Point
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap

@Component
class EventQueryOptionsBuilder {

    private val logger = LoggerFactory.getLogger(EventQueryOptionsBuilder::class.java)

    fun build(queryParams: MultiValueMap<String, String>): EventQueryOptions {
        return EventQueryOptions(
                pageNumber = queryParams.intParam("pageNumber"),
                pageSize = queryParams.intParam("pageSize"),
                sort = queryParams.stringParams("sort"),
                sortDirection = queryParams.getSortDirection("sortDirection"),
                nameLike = queryParams.stringParam("nameLike"),
                boundingSphere = queryParams.boundingSphereParam("boundingSphere"),
                tags = queryParams.stringParams("tag")
        ).also {
            logger.info("Built $it from $queryParams")
        }
    }
}

private fun MultiValueMap<String, String>.boundingSphereParam(paramName: String): Circle? {
    val boundingCircle = this.stringParam(paramName)
    if (boundingCircle != null) {
        try {
            val parts = boundingCircle.split(",")
            val centerLongitude = parts[0].toDouble()
            val centerLatitude = parts[1].toDouble()
            val radiusInMeters = parts[2].toInt()
            return Circle(Point(centerLongitude, centerLatitude), Distance((radiusInMeters / 1000.0), KILOMETERS))
        } catch (exception: Exception) {
            WrongParamFormatException("Param $paramName expects format 'centerLongitude,centerLatitude,radiusInMeters'")
        }
    }
    return null
}

private fun MultiValueMap<String, String>.getSortDirection(paramName: String): Sort.Direction? {
    return this.stringParam(paramName)?.let { dir ->
        Sort.Direction.fromString(dir)
    }
}

private fun MultiValueMap<String, String>.intParam(paramName: String): Int? {
    return try {
        this[paramName]?.get(0)?.toInt()
    } catch (exception: Exception) {
        throw WrongParamFormatException("Param $paramName is expected to be of an integer type.")
    }
}

private fun MultiValueMap<String, String>.stringParam(paramName: String): String? {
    return this[paramName]?.get(0)
}

private fun MultiValueMap<String, String>.stringParams(paramName: String): List<String> {
    return this[paramName] ?: listOf()
}