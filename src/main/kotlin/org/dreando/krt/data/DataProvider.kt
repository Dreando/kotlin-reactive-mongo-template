package org.dreando.krt.data

import org.dreando.krt.event.Event
import org.dreando.krt.event.EventRepository
import org.dreando.krt.event.Tag
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
@Profile("init-db")
class DataProvider(private val eventRepository: EventRepository) {

    private val logger = LoggerFactory.getLogger(DataProvider::class.java)

    private val tags = arrayOf("accident", "road-closure", "marathon", "concert", "fire", "flood")

    private val random = Random()
    private val centerX: Double = 51.758299
    private val centerY: Double = 19.4555023
    private val radiusMeters: Int = 25 * 1000

    @PostConstruct
    fun init() {
        logger.info("Adding some test data to mongo")
        this.initRepo()
    }

    private fun initRepo() {
        eventRepository.insert(IntRange(1, 1000).map { index ->
            buildEvent(index)
        })
    }

    private fun buildEvent(index: Int): Event {
        return Event(
                name = "Event-$index",
                tags = randomTags(),
                point = randomGeoPoint()
        ).also { event ->
            logger.info("Created event: $event")
        }
    }

    private fun randomGeoPoint(): GeoJsonPoint {
        // Convert radius from meters to degrees
        val radiusInDegrees = (radiusMeters / 111000f).toDouble()

        val u = random.nextDouble()
        val v = random.nextDouble()
        val w = radiusInDegrees * Math.sqrt(u)
        val t = 2.0 * Math.PI * v
        val x = w * Math.cos(t)
        val y = w * Math.sin(t)

        // Adjust the x-coordinate for the shrinking of the east-west distances
        val newX = x / Math.cos(Math.toRadians(centerY))
        val foundLongitude = newX + centerX
        val foundLatitude = y + centerY

        return GeoJsonPoint(foundLongitude, foundLatitude)
    }

    private fun randomTags(): List<Tag> {
        return IntRange(0, random.nextInt(tags.size)).map {
            Tag(tags[random.nextInt(6)])
        }.distinct()
    }
}