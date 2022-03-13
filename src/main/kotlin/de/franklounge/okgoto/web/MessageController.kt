package de.franklounge.okgoto.web

import de.franklounge.okgoto.model.Mapper
import de.franklounge.okgoto.model.Message
import org.springframework.web.bind.annotation.*

@RestController
class MessageController(val mapper: Mapper) {
    @GetMapping("/message/{uuid}")
    fun getMessage(@PathVariable("uuid") uuid: String, since: Long): List<Message> {
        return mapper.load(Message::class.java, uuid, since)
    }
}
