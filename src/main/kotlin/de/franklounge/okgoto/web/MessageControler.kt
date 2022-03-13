package de.franklounge

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@RestController
class MessageController(val mapper: Mapper) {
    @GetMapping("/message/{uuid}")
    fun getMessage(@PathVariable("uuid") uuid: String, since: Long): Message {
        return mapper.load(Message::class.java, uuid, since) ?: throw NoSuchElementException()
    }
}

@DynamoDBTable(tableName = "Message")
data class Message(
    var id: String?,
    var ts: Long,
    var location: String?,
    var message: String
)
