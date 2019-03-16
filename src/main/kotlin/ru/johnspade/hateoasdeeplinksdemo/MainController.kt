package ru.johnspade.hateoasdeeplinksdemo

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.Resources
import org.springframework.hateoas.core.Relation
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
class MainController(
		private val penRepo: PenRepository,
		private val bookRepo: BookRepository,
		private val deepLinkRepo: DeepLinkRepository
) {

	@GetMapping
	fun getApi(): ResourceSupport {
		val resource = ResourceSupport()
		resource.add(
				linkTo(methodOn(MainController::class.java).getPens()).withRel("pens"),
				linkTo(methodOn(MainController::class.java).getBooks()).withRel("books"),
				linkTo(methodOn(MainController::class.java).deepLink(null, null)).withRel("deepLink")
		)
		return resource
	}

	@GetMapping("/pens")
	fun getPens(): Resources<PenResource> {
		val pens = penRepo.findAll().map {
			PenResource(it.color, it.deepLink.id).apply {
				links += linkTo(methodOn(MainController::class.java).getPen(it.id)).withSelfRel()
			}
		}
		return Resources(pens)
	}

	@GetMapping("/books")
	fun getBooks(): Resources<BookResource> {
		val books = bookRepo.findAll().map {
			BookResource(it.name, it.deepLink.id).apply {
				links += linkTo(methodOn(MainController::class.java).getBook(it.id)).withSelfRel()
			}
		}
		return Resources(books)
	}

	@GetMapping("/pens/{id}")
	fun getPen(@PathVariable id: UUID): PenResource =  penRepo.getOne(id).run { PenResource(color, deepLink.id) }

	@GetMapping("/books/{id}")
	fun getBook(@PathVariable id: UUID): BookResource = bookRepo.getOne(id).run { BookResource(name, deepLink.id) }

	@GetMapping("/links/{id}")
	fun deepLink(@PathVariable id: UUID?, response: HttpServletResponse?): ResponseEntity<Any> {
		id!!; response!!
		val deepLink = deepLinkRepo.getOne(id)
		val path: String = when (deepLink.entityType) {
			EntityType.PEN -> linkTo(methodOn(MainController::class.java).getPen(deepLink.entityId))
			EntityType.BOOK -> linkTo(methodOn(MainController::class.java).getBook(deepLink.entityId))
		}.toUri().path
		response.sendRedirect(path)
		return ResponseEntity.notFound().build()
	}

}

@Relation(collectionRelation = "entries")
open class PenResource(val color: String, val deepLinkId: UUID): ResourceSupport()

@Relation(collectionRelation = "entries")
open class BookResource(val name: String, val deepLinkId: UUID): ResourceSupport()
