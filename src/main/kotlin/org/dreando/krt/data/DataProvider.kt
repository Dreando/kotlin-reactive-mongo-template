package org.dreando.krt.data

import org.dreando.krt.event.Event
import org.dreando.krt.event.EventRepository
import org.dreando.krt.event.Tag
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit
import java.util.*
import javax.annotation.PostConstruct


@Component
@Profile("init-db")
class DataProvider(private val eventRepository: EventRepository) {

    private val logger = LoggerFactory.getLogger(DataProvider::class.java)

    private val tags = arrayOf("accident", "marathon", "concert", "fire", "flood")

    private val random = Random()
    // Lodz, Poland
    private val centerX: Double = 51.758299
    private val centerY: Double = 19.4555023
    private val radiusMeters: Int = 1000 * 1000

    private val chunkSize = 10000
    private val recordsToInsert = 1000 * 1000 * 10

    @PostConstruct
    fun init() {
        this.initRepo()
    }

    private fun initRepo() {
        val chunks = recordsToInsert / chunkSize
        logger.info("Going to attempt insertion of $chunks chunks, $chunkSize items each.")
        val events = IntRange(1, recordsToInsert).map { index ->
            buildEvent(index)
        }
        logger.info("Created ${events.size} events, now the insertion.")
        events.chunked(chunkSize).forEachIndexed { index, chunk ->
            logger.info("Inserted chunk $index/$chunks of $chunkSize items")
            eventRepository.insert(chunk)
        }
    }

    private fun buildEvent(index: Int): Event {
        return Event(
                name = "Event-$index",
                tags = randomTags(),
                point = randomGeoPoint(),
                lastsFrom = randomDate(LocalDate.of(2017, Month.JANUARY, 1), LocalDate.of(2018, Month.JANUARY, 1)),
                lastsTo = randomDate(LocalDate.of(2018, Month.JANUARY, 2), LocalDate.of(2019, Month.JANUARY, 1))
        )
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

    fun randomDate(from: LocalDate, to: LocalDate): LocalDate {
        val daysTo = ChronoUnit.DAYS.between(from, to)
        return from.plusDays(random.nextInt(daysTo.toInt() + 1).toLong())
    }

    private fun randomTags(): List<Tag> {
        return IntRange(0, random.nextInt(3)).map {
            Tag(tags[random.nextInt(tags.size - 1)])
        }.distinct()
    }
}