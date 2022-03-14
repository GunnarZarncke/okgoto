package de.franklounge.okgoto.web

import de.franklounge.okgoto.service.MatchingService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class ManagementController(val matchingService: MatchingService) {

    @PostMapping("/management/health")
    fun health(): String {
        return "OK"
    }

    @PostMapping("/management/run-matching")
    fun runMatching() {
        return matchingService.runMatching()
    }
}
