package de.franklounge.okgoto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.time.Clock

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


