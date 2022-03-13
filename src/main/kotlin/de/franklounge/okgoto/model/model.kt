package de.franklounge.okgoto.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "Profile")
data class Profile(
    var id: String?,
    var attributes: Map<String, Int>? = mapOf(),
    var visible: List<String>? = listOf(),
    var location: String? = null,
    var last: Long? = null,
    var lat: Double? = null,
    var lon: Double? = null
)

@DynamoDBTable(tableName = "Location")
data class Location(
    var id: String?,
    var attributes: Map<String, Int>? = mapOf(),
    var lat: Double? = null,
    var lon: Double? = null,
    var website: String? = null
)

@DynamoDBTable(tableName = "Message")
data class Message(
    var id: String?,
    var ts: Long,
    var location: String?,
    var message: String
)
