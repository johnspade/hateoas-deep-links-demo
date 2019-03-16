package ru.johnspade.hateoasdeeplinksdemo

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.OneToOne

enum class EntityType { PEN, BOOK }

@Entity
class Pen(val color: String) {

	@Id
	@Column(columnDefinition = "uuid")
	val id: UUID = UUID.randomUUID()
	@OneToOne(cascade = [CascadeType.ALL])
	val deepLink: DeepLink = DeepLink(EntityType.PEN, id)

}

interface PenRepository: JpaRepository<Pen, UUID>

@Entity
class Book(val name: String) {

	@Id
	@Column(columnDefinition = "uuid")
	val id: UUID = UUID.randomUUID()
	@OneToOne(cascade = [CascadeType.ALL])
	val deepLink: DeepLink = DeepLink(EntityType.BOOK, id)

}

interface BookRepository: JpaRepository<Book, UUID>

@Entity
class DeepLink(
		@Enumerated(EnumType.STRING)
		val entityType: EntityType,
		@Column(columnDefinition = "uuid")
		val entityId: UUID
) {

	@Id
	@Column(columnDefinition = "uuid")
	val id: UUID = UUID.randomUUID()

}

interface DeepLinkRepository: JpaRepository<DeepLink, UUID>
