package ru.johnspade.hateoasdeeplinksdemo

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class HateoasDeepLinksDemoApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc
	@Autowired
	private lateinit var bookRepo: BookRepository

	@Test
	fun testDeepLink() {
		val book = bookRepo.save(Book("Alice in Wonderland"))
		mockMvc.perform(get("/api/books/${book.id}")).andExpect(jsonPath("$.name", `is`("Alice in Wonderland")))
		mockMvc.perform(get("/api/links/${book.deepLink.id}")).andExpect(redirectedUrl("/api/books/${book.id}"))
	}

}
