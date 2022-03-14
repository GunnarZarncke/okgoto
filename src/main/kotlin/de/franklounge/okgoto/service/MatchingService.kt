package de.franklounge.okgoto.service

import com.github.jelmerk.knn.hnsw.HnswIndex
import de.franklounge.okgoto.model.Mapper
import de.franklounge.okgoto.model.Profile
import org.springframework.stereotype.Service

@Service
class MatchingService(val mapper: Mapper) {
    fun runMatching() {
        TODO("Not yet implemented")
        //val profiles = mapper.loadAll(Profile::class.java)
    }

    fun doIt(){
        // HnswIndex()
    }
}
