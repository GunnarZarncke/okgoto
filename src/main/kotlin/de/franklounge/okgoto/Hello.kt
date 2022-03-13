package de.franklounge.okgoto

import org.springframework.boot.SpringApplication
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

    SpringApplication.run(OkGoto::class.java, *args)
}
