package org.dreando.krt.config

import org.dreando.krt.exception.WrongParamFormatException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.geo.*
import org.springframework.util.MultiValueMap

typealias QueryParams = MultiValueMap<String, String>

internal fun QueryParams.getBoundingSphere(): Circle? {
    val boundingCircle = this.getString("boundingSphere")
    if (boundingCircle != null) {
        try {
            val parts = boundingCircle.split(",")
            val centerLongitude = parts[0].toDouble()
            val centerLatitude = parts[1].toDouble()
            val radiusInMeters = parts[2].toInt()
            return Circle(Point(centerLongitude, centerLatitude), Distance((radiusInMeters / 1000.0), Metrics.KILOMETERS))
        } catch (exception: Exception) {
            WrongParamFormatException("BoundingSphere expects format 'centerLongitude,centerLatitude,radiusInMeters'")
        }
    }
    return null
}

internal fun QueryParams.getBoundingBox(): Box? {
    val boundingCircle = this.getString("boundingBox")
    if (boundingCircle != null) {
        try {
            val parts = boundingCircle.split(",")
            val firstPointX = parts[0].toDouble()
            val firstPointY = parts[1].toDouble()
            val secondPointX = parts[2].toDouble()
            val secondPointY = parts[3].toDouble()
            return Box(Point(firstPointX, firstPointY), Point(secondPointX, secondPointY))
        } catch (exception: Exception) {
            WrongParamFormatException("BoundingSphere expects format 'centerLongitude,centerLatitude,radiusInMeters'")
        }
    }
    return null
}

internal fun QueryParams.getSort(): Sort? {
    val sortFields = this.getStrings("sort")
    val sortDir = this.getString("sortDir")
    return if (sortDir != null) {
        Sort.by(Sort.Direction.fromString(sortDir), *sortFields.toTypedArray())
    } else null
}

internal fun QueryParams.getPageable(): PageRequest? {
    return try {
        val pageNumber = this["pageNumber"]?.get(0)?.toInt()
        val pageSize = this["pageSize"]?.get(0)?.toInt()
        if (pageNumber != null && pageSize != null) PageRequest.of(pageNumber, pageSize) else null
    } catch (exception: Exception) {
        throw WrongParamFormatException("Param pageNumber and pageSize is expected to be of an integer type.")
    }
}

internal fun QueryParams.getString(paramName: String): String? {
    return this[paramName]?.get(0)
}

internal fun QueryParams.getStrings(paramName: String): List<String> {
    return this[paramName] ?: listOf()
}