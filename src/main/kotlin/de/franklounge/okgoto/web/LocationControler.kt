package de.franklounge

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class LocationController(val mapper: Mapper) {

    @GetMapping("/location/{uuid}")
    fun getLocation(@PathVariable("uuid") uuid: String): Location {
        return mapper.load(Location::class.java, uuid) ?: throw NoSuchElementException()
    }

    @PutMapping("/location/{uuid}")
    fun setLocation(@PathVariable("uuid") uuid: String, @RequestBody Location: Location) {
        val existing = mapper.load(Location::class.java, uuid)
        if (existing != null) {
            if (Location.attributes == null) Location.attributes = existing.attributes
            if (Location.lat == null) Location.lat = existing.lat
            if (Location.lon == null) Location.lon = existing.lon
            if (Location.website == null) Location.website = existing.website
        }
        if (Location.id == null) Location.id = uuid
        if (Location.id != uuid) error("if location contains ID it must match the path")
        save(Location)
    }

    @PostMapping("/location")
    fun createLocation(@PathVariable("uuid") uuid: String, @RequestBody location: Location) {
        if (location.id != uuid) error("if location contains ID it must match the path")
        save(location)
    }

    private fun save(location: Location) {
        if (location.id == null) location.id = "${UUID.randomUUID()}-${UUID.randomUUID()}"
        if (location.attributes == null) location.attributes = mapOf()
        if (location.lat == null || location.lon == null) error("location must have attributes")
        mapper.save(location)
    }
}

@DynamoDBTable(tableName = "Location")
data class Location(
    var id: String?,
    var attributes: Map<String, Int>? = mapOf(),
    var lat: Double? = null,
    var lon: Double? = null,
    var website: String? = null
)
