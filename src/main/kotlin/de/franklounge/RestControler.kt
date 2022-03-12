package de.franklounge

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
 
@RestController
class KotlinRestController {
	
	@GetMapping("/profile/{uuid}")
    // FIXME: fr: [2022-03-12] replae this test-dummy 
	fun getProfile(@PathVariable("uuid") uuid: String) : Profile{
/*
	val item_values = mutableMapOf("id" to AttributeValue(java.util.UUID.randomUUID().toString()),
                                    "attributes" to AttributeValue(""))
    val ddb = AmazonDynamoDBClientBuilder.defaultClient();

    val item = ddb.getItem("id", uuid);
*/
	  return Profile("1")
}
  
  @PutMapping("/profile/{uuid}")
	fun setProfile(@PathVariable("uuid") uuid: String, @RequestBody profile: Profile) {
	/*
	val item_values = mutableMapOf("id" to AttributeValue(java.util.UUID.randomUUID().toString()),
                                    "attributes" to AttributeValue(""))
    val ddb = AmazonDynamoDBClientBuilder.defaultClient();

    val res = ddb.putItem("Profile", item_values);
    println("$res")
 	*/
}
	
}


// FIXME: fr: [2022-03-12] replace this test-dummy 
data class Profile(
		val id: String,
		val attributes: Map<String, Int> = mapOf(),
		val visible: List<String> = listOf(),
		val lat: Double = 53.5,
		val lon: Double = 10.0) {
}
