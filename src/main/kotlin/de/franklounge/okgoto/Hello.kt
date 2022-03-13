package de.franklounge.okgoto

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException
import java.util.ArrayList
import java.time.Clock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class OkGoto {
    @Bean
    open fun systemClock() = Clock.systemUTC()
    
}

fun main(args: Array<String>) {
    println("Ok, go to the net!")
    val text = org.apache.commons.io.IOUtils.toString(java.io.FileInputStream("/data/person/Arthur Schopenhauer"), "UTF-8")
    val res = "\\p{L}+".toRegex().findAll(text).map{it.value}.toSet()
    print("${res}")
    
    //SpringApplication.run(OkGoto::class.java, *args)
    /*
    val item_values = mutableMapOf("id" to AttributeValue(java.util.UUID.randomUUID().toString()),
                                    "attributes" to AttributeValue(""))
    val ddb = AmazonDynamoDBClientBuilder.defaultClient();

    val res = ddb.putItem("Profile", item_values);
    println("$res")
    */
}


