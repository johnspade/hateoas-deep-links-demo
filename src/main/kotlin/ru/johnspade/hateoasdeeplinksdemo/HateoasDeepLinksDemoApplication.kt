package ru.johnspade.hateoasdeeplinksdemo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HateoasDeepLinksDemoApplication(
		private val penRepo: PenRepository,
		private val bookRepo: BookRepository
): CommandLineRunner {

	override fun run(vararg args: String?) {
		penRepo.saveAll(listOf(Pen("red"), Pen("green")))
		bookRepo.saveAll(listOf(Book("Harry Potter"), Book("Cryptonomicon")))
	}

}

fun main(args: Array<String>) {
	runApplication<HateoasDeepLinksDemoApplication>(*args)
}
