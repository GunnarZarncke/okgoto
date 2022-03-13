package de.franklounge.okgoto.web

import de.franklounge.Mapper
import de.franklounge.okgoto.model.Profile
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ProfileController(val mapper: Mapper) {

    @GetMapping("/profile/{uuid}")
    fun getProfile(@PathVariable("uuid") uuid: String): Profile {
        return mapper.load(Profile::class.java, uuid) ?: throw NoSuchElementException()
    }

    @PutMapping("/profile/{uuid}")
    fun setProfile(@PathVariable("uuid") uuid: String, @RequestBody profile: Profile) {
        val existing = mapper.load(Profile::class.java, uuid)
        if (existing != null) {
            if (profile.attributes == null) profile.attributes = existing.attributes
            if (profile.visible == null) profile.visible = existing.visible
            if (profile.lat == null) profile.lat = existing.lat
            if (profile.lon == null) profile.lon = existing.lon
            if (profile.last == null) profile.last = existing.last
            if (profile.location == null) profile.location = existing.location
        }
        if (profile.id == null) profile.id = uuid
        if (profile.id != uuid) error("if profile contains ID it must match the path")
        save(profile)
    }

    @PostMapping("/profile")
    fun createProfile(@PathVariable("uuid") uuid: String, @RequestBody profile: Profile) {
        if (profile.id != uuid) error("if profile contains ID it must match the path")
        save(profile)
    }

    private fun save(profile: Profile) {
        if (profile.id == null) profile.id = "${UUID.randomUUID()}-${UUID.randomUUID()}"
        if (profile.attributes == null) error("profile must have attributes")
        if (profile.visible == null) profile.visible = listOf()
        mapper.save(profile)
    }
}
