package de.franklounge.okgoto.model

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import org.springframework.stereotype.Service

interface Mapper {
    abstract fun <T> load(java: Class<T>, uuid: String): T?
    abstract fun <T> load(java: Class<T>, uuid: String, sort: Long): List<T>
    abstract fun <T> save(bean: T)
}

@Service
class MapperImpl : Mapper {
    private val ddb = AmazonDynamoDBClientBuilder.defaultClient()
    private val mapper = DynamoDBMapper(ddb)
    override fun <T> save(bean: T) {
        mapper.save(bean)
    }

    override fun <T> load(javaClass: Class<T>, uuid: String): T? {
        return mapper.load(javaClass, uuid)
    }

    override fun <T> load(javaClass: Class<T>, uuid: String, sort: Long): List<T> {
        val expressionAttributesNames = mutableMapOf<String, String>()
        expressionAttributesNames["#id"] = "id"
        expressionAttributesNames["#ts"] = "ts"

        val expressionAttributeValues = mutableMapOf<String, AttributeValue>()
        expressionAttributeValues[":id"] = AttributeValue().withS(uuid)
        expressionAttributeValues[":from"] = AttributeValue().withN(sort.toString())

        val queryExpression: DynamoDBQueryExpression<T> = DynamoDBQueryExpression<T>()
            .withKeyConditionExpression("#id = :id and #ts GREATER :from")
            .withExpressionAttributeNames(expressionAttributesNames)
            .withExpressionAttributeValues(expressionAttributeValues)

        return mapper.query(javaClass, queryExpression)
    }
}
