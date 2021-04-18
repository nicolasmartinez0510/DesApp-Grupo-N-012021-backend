package ar.edu.unq.grupoN.backenddesappapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
class BackendDesappApiApplication

@RestController
class HelloController{
	@GetMapping("/")
	fun sayHello(@RequestParam(value = "myName", defaultValue = "World") name: String?): String? {
		return String.format("Hello %s!", name)
	}
}

fun main(args: Array<String>) {
	runApplication<BackendDesappApiApplication>(*args)
}

